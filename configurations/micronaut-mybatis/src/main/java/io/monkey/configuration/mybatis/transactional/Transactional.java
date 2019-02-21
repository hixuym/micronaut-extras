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

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;
import org.apache.ibatis.session.ExecutorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Michael
 * Created at: 2019/2/19 16:47
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Around
@Type(LocalTransactionalInterceptor.class)
public @interface Transactional {

    /**
     * Returns the constant indicating the myBatis executor type.
     *
     * @return the constant indicating the myBatis executor type.
     */
    ExecutorType executorType() default ExecutorType.SIMPLE;

    /**
     * Returns the constant indicating the transaction isolation level.
     *
     * @return the constant indicating the transaction isolation level.
     */
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * Flag to indicate that myBatis has to force the transaction {@code commit().}
     *
     * @return false by default, user defined otherwise.
     */
    boolean force() default false;

    /**
     * The exception re-thrown when an error occurs during the transaction.
     *
     * @return the exception re-thrown when an error occurs during the transaction.
     */
    Class<? extends Throwable> rethrowExceptionsAs() default Exception.class;

    /**
     * A custom error message when throwing the custom exception.
     *
     * It supports java.util.Formatter place holders, intercepted method arguments will be used as message format
     * arguments.
     *
     * @return a custom error message when throwing the custom exception.
     * @see java.util.Formatter#format(String, Object...)
     */
    String exceptionMessage() default "";

    /**
     * If true, the transaction will never committed but rather rolled back, useful for testing purposes.
     *
     * This parameter is false by default.
     *
     * @return if true, the transaction will never committed but rather rolled back, useful for testing purposes.
     */
    boolean rollbackOnly() default false;

    //
    // from javax.transaction.Transactional
    //

    /**
     * The TxType element of the Transactional annotation indicates whether a bean method is to be executed within a
     * transaction context.
     */
    TxType value() default TxType.REQUIRED;

    /**
     * The TxType element of the annotation indicates whether a bean method is to be executed within a transaction context
     * where the values provide the following corresponding behavior.
     */
    enum TxType {
        /**
         * <p>
         * If called outside a transaction context, the interceptor must begin a new JTA transaction, the managed bean
         * method execution must then continue inside this transaction context, and the transaction must be completed by the
         * interceptor.
         * </p>
         * <p>
         * If called inside a transaction context, the managed bean method execution must then continue inside this
         * transaction context.
         * </p>
         */
        REQUIRED,

        /**
         * <p>
         * If called outside a transaction context, the interceptor must begin a new JTA transaction, the managed bean
         * method execution must then continue inside this transaction context, and the transaction must be completed by the
         * interceptor.
         * </p>
         * <p>
         * If called inside a transaction context, the current transaction context must be suspended, a new JTA transaction
         * will begin, the managed bean method execution must then continue inside this transaction context, the transaction
         * must be completed, and the previously suspended transaction must be resumed.
         * </p>
         */
        REQUIRES_NEW,

        /**
         * <p>
         * If called outside a transaction context, a TransactionalException with a nested TransactionRequiredException must
         * be thrown.
         * </p>
         * <p>
         * If called inside a transaction context, managed bean method execution will then continue under that context.
         * </p>
         */
        MANDATORY,

        /**
         * <p>
         * If called outside a transaction context, managed bean method execution must then continue outside a transaction
         * context.
         * </p>
         * <p>
         * If called inside a transaction context, the managed bean method execution must then continue inside this
         * transaction context.
         * </p>
         */
        SUPPORTS,

        /**
         * <p>
         * If called outside a transaction context, managed bean method execution must then continue outside a transaction
         * context.
         * </p>
         * <p>
         * If called inside a transaction context, the current transaction context must be suspended, the managed bean
         * method execution must then continue outside a transaction context, and the previously suspended transaction must
         * be resumed by the interceptor that suspended it after the method execution has completed.
         * </p>
         */
        NOT_SUPPORTED,

        /**
         * <p>
         * If called outside a transaction context, managed bean method execution must then continue outside a transaction
         * context.
         * </p>
         * <p>
         * If called inside a transaction context, a TransactionalException with a nested InvalidTransactionException must
         * be thrown.
         * </p>
         */
        NEVER
    }

    /**
     * The rollbackOn element can be set to indicate exceptions that must cause the interceptor to mark the transaction
     * for rollback. Conversely, the dontRollbackOn element can be set to indicate exceptions that must not cause the
     * interceptor to mark the transaction for rollback. When a class is specified for either of these elements, the
     * designated behavior applies to subclasses of that class as well. If both elements are specified, dontRollbackOn
     * takes precedence.
     *
     * @return Class[] of Exceptions
     */
    // @Nonbinding
    // public Class[] rollbackOn() default {};

    /**
     * The dontRollbackOn element can be set to indicate exceptions that must not cause the interceptor to mark the
     * transaction for rollback. Conversely, the rollbackOn element can be set to indicate exceptions that must cause the
     * interceptor to mark the transaction for rollback. When a class is specified for either of these elements, the
     * designated behavior applies to subclasses of that class as well. If both elements are specified, dontRollbackOn
     * takes precedence.
     *
     * @return Class[] of Exceptions
     */
    // @Nonbinding
    // public Class[] dontRollbackOn() default {};

}
