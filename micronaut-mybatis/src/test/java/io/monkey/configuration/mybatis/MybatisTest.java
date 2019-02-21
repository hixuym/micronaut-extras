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

package io.monkey.configuration.mybatis;

import io.micronaut.context.ApplicationContext;
import io.monkey.configuration.mybatis.model.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Michael
 * Created at: 2019/2/19 21:48
 */
public class MybatisTest {

    @Test
    public void testMybatis() {
        try(ApplicationContext context = ApplicationContext.run()) {

            UserService userRepository = context.getBean(UserService.class);

            userRepository.createTable();

            Assertions.assertEquals(0, userRepository.findUserCount(100));

        }
    }
}
