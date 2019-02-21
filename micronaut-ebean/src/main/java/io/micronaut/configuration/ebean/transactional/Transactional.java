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

import io.ebean.annotation.PersistBatch;
import io.ebean.annotation.TxIsolation;
import io.ebean.annotation.TxType;
import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify transaction scoping for a method.
 * <p> <b><i> This is only supported if "Enhancement" is
 * used via javaagent, ANT task or IDE enhancement plugin etc. </i></b> </p>
 *
 * <p> Note: Currently
 * there are 3 known annotations that perform this role.
 * <ul>
 * <li>EJB's javax.ejb.TransactionAttribute</li>
 * <li>Spring's org.springframework.transaction.annotation.Transactional</li>
 * <li>and this one, Ebean's own Transactional</li>
 * </ul>
 * <p>
 * Spring created their one because the EJB
 * annotation does not support features such as isolation level and specifying rollbackOn,
 * noRollbackOn exceptions. This one exists for Ebean because I agree that the standard one is
 * insufficient and don't want to include a dependency on Spring. </p>
 *
 * <p> The default behaviour of
 * EJB (and hence Spring) is to NOT ROLLBACK on checked exceptions. I find this very
 * counter-intuitive. Ebean will provide a property to set the default behaviour to rollback on any
 * exception and optionally change the setting to be consistent with EJB/Spring if people wish to do
 * so. </p>
 * <pre>{@code
 *
 *  // a normal class
 *  public class MySimpleUserService {
 *
 *    // this method is transactional automatically handling
 *    // transaction begin, commit and rollback etc
 *    Transactional
 *    public void runInTrans() throws IOException {
 *
 *      // tasks performed within the transaction
 *      ...
 *      // find some objects
 *      Customer cust = ebeanServer.find(Customer.class, 42);
 *
 *      Order order = ...;
 *      ...
 *      // save some objects
 *      ebeanServer.save(customer);
 *      ebeanServer.save(order);
 *    }
 *
 * }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Around
@Type(LocalTransactionalInterceptor.class)
public @interface Transactional {

    /**
     * The type of transaction scoping. Defaults to REQUIRED.
     * @return type
     */
    TxType type() default TxType.REQUIRED;

    /**
     * Persist batch mode for the transaction.
     * @return batch
     */
    PersistBatch batch() default PersistBatch.INHERIT;

    /**
     * Persist batch mode for the request if not set on the transaction. <p> If batch is set to NONE
     * then batchOnCascade can be set to INSERT or ALL and then each save(), delete(), insert(),
     * update() request that cascades to child beans can use JDBC batch. </p>
     * @return batchOnCascade
     */
    PersistBatch batchOnCascade() default PersistBatch.INHERIT;

    /**
     * The batch size to use when using JDBC batch mode. <p> If unset this defaults to the value set
     * in ServerConfig. </p>
     * @return batchSize
     */
    int batchSize() default 0;

    /**
     * Set to false when we want to skip getting generatedKeys. <p> This is typically used in the case
     * of large batch inserts where we get a performance benefit from not calling getGeneratedKeys (as
     * we are going to insert a lot of rows and have no need for the Id values after the insert).
     * </p>
     * @return getGeneratedKeys
     */
    boolean getGeneratedKeys() default true;

    /**
     * The transaction isolation level this transaction should have. <p> This will only be used if
     * this scope creates the transaction. If the transaction has already started then this will
     * currently be ignored (you could argue that it should throw an exception). </p>
     */
    TxIsolation isolation() default TxIsolation.DEFAULT;

    /**
     * Set this to false if the JDBC batch should not be automatically flushed when a query is executed.
     */
    boolean flushOnQuery() default true;

    /**
     * Set this to true if the transaction should be only contain queries.
     */
    boolean readOnly() default false;

    /**
     * The name of the server that you want the transaction to be created from. <p> If left blank the
     * 'default' server is used. </p>
     */
    String dbName() default "";

    /**
     * Set this to true such that the L2 cache is not used by queries that otherwise would.
     */
    boolean skipCache() default false;

    /**
     * Set a label to identify the transaction in performance metrics and logging.
     */
    String label() default "";

    /**
     * The Throwable's that will explicitly cause a rollback to occur.
     */
    Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * The Throwable's that will explicitly NOT cause a rollback to occur.
     */
    Class<? extends Throwable>[] noRollbackFor() default {};

    /**
     * A key used to identify a specific transaction for profiling purposes.
     * <p>
     * If set to -1 this means there should be no profiling on this transaction.
     * </p>
     * <p>
     * If not set (left at 0) this means the profilingId can be automatically set during transactional enhancement.
     * </p>
     */
    int profileId() default 0;
}
