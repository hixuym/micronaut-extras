/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.monkey.configuration.mybatis.transactional;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import io.monkey.configuration.mybatis.SqlSessionManager;
import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

/**
 * @author Michael
 * Created at: 2019/2/19 18:00
 */
@Singleton
class LocalTransactionalInterceptor implements MethodInterceptor<Object, Object> {

    private static final Logger log = LoggerFactory.getLogger(LocalTransactionalInterceptor.class);

    private static final Class<?>[] CAUSE_TYPES = new Class[]{Throwable.class};

    private static final Class<?>[] MESSAGE_CAUSE_TYPES = new Class[]{String.class, Throwable.class};

    private final SqlSessionManager sqlSessionManager;

    public LocalTransactionalInterceptor(SqlSessionManager sqlSessionManager) {
        this.sqlSessionManager = sqlSessionManager;
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {

        AnnotationValue<Transactional> txn = context.getExecutableMethod().findAnnotation(Transactional.class)
            .orElse(context.getAnnotation(Transactional.class));

        String debugPrefix = null;
        if (log.isDebugEnabled()) {
            debugPrefix = format("[Intercepted method: %s]", context.getMethodName());
        }

        boolean isSessionInherited = this.sqlSessionManager.isManagedSessionStarted();

        if (isSessionInherited) {
            if (log.isDebugEnabled()) {
                log.debug(format("%s - SqlSession already set for thread: %s", debugPrefix, currentThread().getId()));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(format("%s - SqlSession not set for thread: %s, creating a new one", debugPrefix, currentThread().getId()));
            }

            ExecutorType executorType = txn.get("executorType", ExecutorType.class, ExecutorType.SIMPLE);
            Isolation isolation = txn.get("isolation", Isolation.class, Isolation.DEFAULT);
            sqlSessionManager.startManagedSession(executorType, isolation.getTransactionIsolationLevel());
        }

        boolean needsRollback = txn.get("rollbackOnly", boolean.class, false);
        boolean force = txn.get("force", boolean.class, false);

        Object object;
        try {
            object = context.proceed();
        } catch (Throwable t) {
            needsRollback = true;
            throw new RuntimeException(convertThrowableIfNeeded(context, txn, t));
        } finally {
            if (!isSessionInherited) {
                try {
                    if (needsRollback) {
                        if (log.isDebugEnabled()) {
                            log.debug(debugPrefix + " - SqlSession of thread: " + currentThread().getId() + " rolling back");
                        }

                        sqlSessionManager.currentManagedSession().ifPresent(it -> it.rollback(true));
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug(debugPrefix + " - SqlSession of thread: " + currentThread().getId() + " committing");
                        }

                        sqlSessionManager.currentManagedSession().ifPresent(it -> it.commit(force));
                    }
                } finally {
                    if (log.isDebugEnabled()) {
                        log.debug(format("%s - SqlSession of thread: %s terminated its life-cycle, closing it", debugPrefix,
                            currentThread().getId()));
                    }

                    sqlSessionManager.closeManagedSession();
                }
            } else if (log.isDebugEnabled()) {
                log.debug(format("%s - SqlSession of thread: %s is inherited, skipped close operation", debugPrefix,
                    currentThread().getId()));
            }
        }

        return object;
    }

    private Throwable convertThrowableIfNeeded(MethodInvocationContext<Object, Object> invocation, AnnotationValue<Transactional> transactional, Throwable t) {
        Method interceptedMethod = invocation.getTargetMethod();

        // check the caught exception is declared in the invoked method
        for (Class<?> exceptionClass : interceptedMethod.getExceptionTypes()) {
            if (exceptionClass.isAssignableFrom(t.getClass())) {
                return t;
            }
        }

        Class<? extends Throwable> rethrowExceptionsAs =
            transactional.get("rethrowExceptionsAs", Class.class, Exception.class);

        // check the caught exception is of same rethrow type
        if (rethrowExceptionsAs.isAssignableFrom(t.getClass())) {
            return t;
        }

        // rethrow the exception as new exception
        String errorMessage;
        Object[] initargs;
        Class<?>[] initargsType;

        String exceptionMessage = transactional.get("exceptionMessage", String.class, "");

        if (exceptionMessage.length() != 0) {
            errorMessage = format(exceptionMessage, invocation.getParameterValues());
            initargs = new Object[] { errorMessage, t };
            initargsType = MESSAGE_CAUSE_TYPES;
        } else {
            initargs = new Object[] { t };
            initargsType = CAUSE_TYPES;
        }

        Constructor<? extends Throwable> exceptionConstructor = getMatchingConstructor(rethrowExceptionsAs,
            initargsType);
        Throwable rethrowEx;
        if (exceptionConstructor != null) {
            try {
                rethrowEx = exceptionConstructor.newInstance(initargs);
            } catch (Exception e) {
                errorMessage = format("Impossible to re-throw '%s', it needs the constructor with %s argument(s).",
                    rethrowExceptionsAs.getName(), Arrays.toString(initargsType));
                log.error(errorMessage, e);
                rethrowEx = new RuntimeException(errorMessage, e);
            }
        } else {
            errorMessage = format("Impossible to re-throw '%s', it needs the constructor with %s or %s argument(s).",
                rethrowExceptionsAs.getName(), Arrays.toString(CAUSE_TYPES),
                Arrays.toString(MESSAGE_CAUSE_TYPES));
            log.error(errorMessage);
            rethrowEx = new RuntimeException(errorMessage);
        }

        return rethrowEx;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> Constructor<E> getMatchingConstructor(Class<E> type, Class<?>[] argumentsType) {
        Class<? super E> currentType = type;
        while (Object.class != currentType) {
            for (Constructor<?> constructor : currentType.getConstructors()) {
                if (Arrays.equals(argumentsType, constructor.getParameterTypes())) {
                    return (Constructor<E>) constructor;
                }
            }
            currentType = currentType.getSuperclass();
        }
        return null;
    }
}
