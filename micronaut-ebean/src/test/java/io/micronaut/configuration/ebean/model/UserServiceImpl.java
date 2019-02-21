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

package io.micronaut.configuration.ebean.model;

import io.ebean.EbeanServer;
import io.micronaut.configuration.ebean.transactional.Transactional;

import javax.inject.Singleton;

/**
 * @author Michael
 * Created at: 2019/2/18 22:19
 */
@Singleton
public class UserServiceImpl implements UserService {

    private final EbeanServer ebeanServer;

    public UserServiceImpl(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    @Transactional
    @Override
    public int saveAndQueryUser() {

        User user = new User();

        user.setAge(10);
        user.setName("michael");

        ebeanServer.save(user);

        return ebeanServer.find(User.class).findCount();
    }
}
