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
import io.micronaut.configuration.ebean.model.User;
import io.micronaut.configuration.ebean.model.UserService;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Michael
 * Created at: 2019/2/18 16:54
 */
public class EbeanServerTest {

    @Test
    void testDataSourceSetup() {
        try (ApplicationContext context = ApplicationContext.run()) {
            DataSource dataSource = context.getBean(DataSource.class);
            Assertions.assertNotNull(dataSource);
        }
    }

    @Test
    void testEbeanServerSetup() {
        try (ApplicationContext context = ApplicationContext.run()) {
            EbeanServer ebeanServer = context.getBean(EbeanServer.class);
            Assertions.assertNotNull(ebeanServer);
        }
    }

    @Test
    void testEbeanServerWork() {
        try (ApplicationContext context = ApplicationContext.run()) {
            EbeanServer ebeanServer = context.getBean(EbeanServer.class);
            List<User> user = ebeanServer.find(User.class).findList();
            Assertions.assertEquals(0, user.size());
        }
    }

    @Test
    void testTxnWork() {
        try (ApplicationContext context = ApplicationContext.run()) {
            UserService userService = context.getBean(UserService.class);
            Assertions.assertEquals(1, userService.saveAndQueryUser());
        }
    }

}
