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

import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.micronaut.context.BeanLocator;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.inject.qualifiers.Qualifiers;

import javax.sql.DataSource;

/**
 * A factory bean for constructing the {@link io.ebean.EbeanServer}.
 *
 * @author Michael
 * Created at: 2019/2/18 16:03
 */
@Factory
class EbeanServerFactoryBean {

    private final EbeanConfiguration ebeanConfiguration;
    private final BeanLocator beanLocator;

    /**
     * @param ebeanConfiguration The Ebean configuration
     * @param beanLocator      The bean locator
     */
    public EbeanServerFactoryBean(EbeanConfiguration ebeanConfiguration,
                                  BeanLocator beanLocator) {
        this.ebeanConfiguration = ebeanConfiguration;
        this.beanLocator = beanLocator;
    }

    /**
     * Builds the {@link EbeanServer} bean for the given {@link DataSource}.
     *
     * @param dataSourceName The data source name
     * @param dataSource     The data source
     * @return The {@link EbeanServer}
     */
    @EachBean(DataSource.class)
    protected EbeanServer ebeanServer(@Parameter String dataSourceName,
                                      DataSource dataSource) {

        EbeanConfiguration ebeanConfiguration = beanLocator.findBean(EbeanConfiguration.class,
            Qualifiers.byName(dataSourceName)).orElse(this.ebeanConfiguration);

        ServerConfig serverConfig = ebeanConfiguration.buildServerConfig();
        serverConfig.setDataSource(dataSource);

        return EbeanServerFactory.create(serverConfig);
    }

}
