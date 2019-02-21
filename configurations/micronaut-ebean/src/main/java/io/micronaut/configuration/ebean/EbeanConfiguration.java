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

package io.micronaut.configuration.ebean;

import io.ebean.annotation.PersistBatch;
import io.ebean.config.ServerConfig;
import io.ebeaninternal.server.lib.ShutdownManager;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.convert.format.MapFormat;
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.jdbc.BasicJdbcConfiguration;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration
 * @author Michael
 * Created at: 2019/2/18 16:02
 */
@EachProperty(value = EbeanConfiguration.PREFIX, primary = "default")
@Requires(property = BasicJdbcConfiguration.PREFIX + ".default")
class EbeanConfiguration implements AutoCloseable {

    static final String PREFIX = "ebean";

    private final String name;

    private boolean defaultServer = true;
    private boolean runMigration = true;

    private boolean ddlGenerate;
    private boolean ddlRun;
    private boolean ddlCreateOnly;

    private boolean ddlExtra;
    private String ddlInitSql;
    private String ddlSeedSql;

    private String[] packagesToScan;

    private String databasePlatformName;
    private PersistBatch persistBatch = PersistBatch.NONE;
    private PersistBatch persistBatchOnCascade = PersistBatch.INHERIT;

    private int persistBatchSize = 20;
    private int lazyLoadBatchSize = 10;
    private int queryBatchSize = 100;

    private int jdbcFetchSizeFindList = 0;
    private int jdbcFetchSizeFindEach = 100;


    private Map<String, String> ebeanProperties = new HashMap<>();

    public EbeanConfiguration(@Parameter String name) {
        this.name = name;
    }

    /**
     * build ebean server config
     * @return ebean server config
     */
    ServerConfig buildServerConfig() {

        ServerConfig serverConfig = new ServerConfig();

        Properties properties = new Properties();

        for (Map.Entry<String, String> entry : ebeanProperties.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }

        serverConfig.loadFromProperties(properties);
        serverConfig.setName(name);
        serverConfig.setDefaultServer(defaultServer);
        serverConfig.setRegister(true);
        serverConfig.setDatabasePlatformName(databasePlatformName);
        serverConfig.setPersistBatch(persistBatch);
        serverConfig.setPersistBatchSize(persistBatchSize);
        serverConfig.setPersistBatchOnCascade(persistBatchOnCascade);

        serverConfig.setLazyLoadBatchSize(lazyLoadBatchSize);
        serverConfig.setQueryBatchSize(queryBatchSize);
        serverConfig.setJdbcFetchSizeFindList(jdbcFetchSizeFindList);
        serverConfig.setJdbcFetchSizeFindEach(jdbcFetchSizeFindEach);

        serverConfig.setDdlGenerate(ddlGenerate);
        serverConfig.setDdlRun(ddlRun);
        serverConfig.setDdlCreateOnly(ddlCreateOnly);
        serverConfig.setDdlInitSql(ddlInitSql);
        serverConfig.setDdlExtra(ddlExtra);
        serverConfig.setDdlSeedSql(ddlSeedSql);

        serverConfig.setRunMigration(runMigration);

        if (ArrayUtils.isNotEmpty(packagesToScan)) {
            for (String pkg : packagesToScan) {
                serverConfig.addPackage(pkg);
            }
        }

        return serverConfig;
    }

    /**
     * Sets the packages to scan.
     *
     * @param packagesToScan The packages to scan
     */
    public void setPackagesToScan(String... packagesToScan) {
        if (ArrayUtils.isNotEmpty(packagesToScan)) {
            this.packagesToScan = packagesToScan;
        }
    }

    /**
     * @return The packages to scan
     */
    public String[] getPackagesToScan() {
        return packagesToScan;
    }

    public boolean isDefaultServer() {
        return defaultServer;
    }

    public void setDefaultServer(boolean defaultServer) {
        this.defaultServer = defaultServer;
    }

    public boolean isRunMigration() {
        return runMigration;
    }

    public void setRunMigration(boolean runMigration) {
        this.runMigration = runMigration;
    }

    public String getDatabasePlatformName() {
        return databasePlatformName;
    }

    public void setDatabasePlatformName(String databasePlatformName) {
        this.databasePlatformName = databasePlatformName;
    }

    public PersistBatch getPersistBatch() {
        return persistBatch;
    }

    public void setPersistBatch(PersistBatch persistBatch) {
        this.persistBatch = persistBatch;
    }

    public PersistBatch getPersistBatchOnCascade() {
        return persistBatchOnCascade;
    }

    public void setPersistBatchOnCascade(PersistBatch persistBatchOnCascade) {
        this.persistBatchOnCascade = persistBatchOnCascade;
    }

    public int getPersistBatchSize() {
        return persistBatchSize;
    }

    public void setPersistBatchSize(int persistBatchSize) {
        this.persistBatchSize = persistBatchSize;
    }

    public int getLazyLoadBatchSize() {
        return lazyLoadBatchSize;
    }

    public void setLazyLoadBatchSize(int lazyLoadBatchSize) {
        this.lazyLoadBatchSize = lazyLoadBatchSize;
    }

    public int getQueryBatchSize() {
        return queryBatchSize;
    }

    public void setQueryBatchSize(int queryBatchSize) {
        this.queryBatchSize = queryBatchSize;
    }

    public int getJdbcFetchSizeFindList() {
        return jdbcFetchSizeFindList;
    }

    public void setJdbcFetchSizeFindList(int jdbcFetchSizeFindList) {
        this.jdbcFetchSizeFindList = jdbcFetchSizeFindList;
    }

    public int getJdbcFetchSizeFindEach() {
        return jdbcFetchSizeFindEach;
    }

    public void setJdbcFetchSizeFindEach(int jdbcFetchSizeFindEach) {
        this.jdbcFetchSizeFindEach = jdbcFetchSizeFindEach;
    }

    public boolean isDdlGenerate() {
        return ddlGenerate;
    }

    public void setDdlGenerate(boolean ddlGenerate) {
        this.ddlGenerate = ddlGenerate;
    }

    public boolean isDdlRun() {
        return ddlRun;
    }

    public void setDdlRun(boolean ddlRun) {
        this.ddlRun = ddlRun;
    }

    public boolean isDdlCreateOnly() {
        return ddlCreateOnly;
    }

    public void setDdlCreateOnly(boolean ddlCreateOnly) {
        this.ddlCreateOnly = ddlCreateOnly;
    }

    public boolean isDdlExtra() {
        return ddlExtra;
    }

    public void setDdlExtra(boolean ddlExtra) {
        this.ddlExtra = ddlExtra;
    }

    public String getDdlInitSql() {
        return ddlInitSql;
    }

    public void setDdlInitSql(String ddlInitSql) {
        this.ddlInitSql = ddlInitSql;
    }

    public String getDdlSeedSql() {
        return ddlSeedSql;
    }

    public void setDdlSeedSql(String ddlSeedSql) {
        this.ddlSeedSql = ddlSeedSql;
    }

    public Map<String, String> getProperties() {
        return ebeanProperties;
    }

    public void setProperties(@MapFormat(transformation = MapFormat.MapTransformation.FLAT) Map<String, String> ebeanProperties) {
        this.ebeanProperties = ebeanProperties;
    }

    @PreDestroy
    @Override
    public void close() {
        ShutdownManager.shutdown();
    }
}
