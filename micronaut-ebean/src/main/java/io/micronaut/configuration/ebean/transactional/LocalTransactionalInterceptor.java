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

package io.micronaut.configuration.ebean.transactional;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.TxScope;
import io.ebean.annotation.PersistBatch;
import io.ebean.annotation.TxIsolation;
import io.ebean.annotation.TxType;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;

import javax.inject.Singleton;

/**
 * @author Michael
 * Created at: 2019/2/18 22:28
 */
@Singleton
class LocalTransactionalInterceptor implements MethodInterceptor<Object, Object> {

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {

        if (context.hasAnnotation(Transactional.class)) {
            AnnotationValue<Transactional> txn = context.getExecutableMethod().getAnnotation(Transactional.class);

            TxScope txScope = new TxScope();

            txn.get("type", TxType.class).ifPresent(txScope::setType);
            txn.get("batch", PersistBatch.class).ifPresent(txScope::setBatch);
            txn.get("batchOnCascade", PersistBatch.class).ifPresent(txScope::setBatchOnCascade);
            txn.get("batchSize", int.class).ifPresent(txScope::setBatchSize);
            txn.get("isolation", TxIsolation.class).ifPresent(txScope::setIsolation);
            txn.get("flushOnQuery", boolean.class).ifPresent(txScope::setFlushOnQuery);
            txn.get("readOnly", boolean.class).ifPresent(txScope::setReadOnly);
            txn.get("skipCache", boolean.class).ifPresent(txScope::setSkipCache);
            txn.get("label", String.class).ifPresent(txScope::setLabel);
            txn.get("profileId", int.class).ifPresent(txScope::setProfileId);
            txn.get("rollbackFor", Class[].class).ifPresent(txScope::setRollbackFor);
            txn.get("noRollbackFor", Class[].class).ifPresent(txScope::setNoRollbackFor);
            txn.get("dbName", String.class).ifPresent(txScope::setServerName);
            txn.get("getGeneratedKeys", boolean.class).ifPresent(b -> {
                if (!b) {
                    txScope.setSkipGeneratedKeys();
                }
            });

            EbeanServer ebeanServer = Ebean.getServer(txScope.getServerName());

            return ebeanServer.executeCall(txScope, () -> {
                try {
                    return context.proceed();
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }
            });
        } else {
            return context.proceed();
        }
    }

}
