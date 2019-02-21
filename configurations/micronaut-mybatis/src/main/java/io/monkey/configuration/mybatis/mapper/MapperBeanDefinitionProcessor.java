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

package io.monkey.configuration.mybatis.mapper;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.processor.BeanDefinitionProcessor;
import io.micronaut.inject.BeanDefinition;
import org.apache.ibatis.session.Configuration;

/**
 * @author Michael
 * Created at: 2019/2/20 12:53
 */
@Context
class MapperBeanDefinitionProcessor implements BeanDefinitionProcessor<Mapper> {

    private final Configuration configuration;

    public MapperBeanDefinitionProcessor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void process(BeanDefinition<?> beanDefinition, BeanContext object) {

        Class<?>[] interfaces = beanDefinition.getBeanType().getInterfaces();

        for (Class<?> mapperClass : interfaces) {
            if (mapperClass.isAnnotationPresent(Mapper.class)) {
                configuration.addMapper(mapperClass);
            }
        }

    }

}
