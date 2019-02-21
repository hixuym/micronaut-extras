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

package io.monkey.configuration.mybatis.model;

import io.monkey.configuration.mybatis.transactional.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Michael
 * Created at: 2019/2/20 17:05
 */
@Singleton
public class UserService {

    @Inject private UserMapper userMapper;

    @Transactional
    public int findUserCount(int id) {
        return userMapper.findUserCount(id);
    }

    @Transactional
    public void createTable() {
        userMapper.createTable();
    }
}
