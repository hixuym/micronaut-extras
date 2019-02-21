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

import io.monkey.configuration.mybatis.mapper.Mapper;
import io.monkey.configuration.mybatis.model.CrudService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author Michael
 * Created at: 2019/2/19 23:02
 */
@Mapper
public interface UserMapper extends CrudService {

    void createTable();

    @Select("select count(*) from users where id = ${id}")
    Integer findUserCount(@Param("id") int userId);

    void save(Map<String, String> user);
}
