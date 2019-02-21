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

import org.apache.ibatis.session.*;

import javax.inject.Singleton;
import java.sql.Connection;
import java.util.Optional;

/**
 * @author Michael
 * Created at: 2019/2/20 14:45
 */
@Singleton
public class SqlSessionManager {

    private final static ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();

    private final SqlSessionFactory sqlSessionFactory;

    public SqlSessionManager(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void startManagedSession() {
        localSqlSession.set(sqlSessionFactory.openSession());
    }

    public void startManagedSession(boolean autoCommit) {
        localSqlSession.set(sqlSessionFactory.openSession(autoCommit));
    }

    public void startManagedSession(Connection connection) {
        localSqlSession.set(sqlSessionFactory.openSession(connection));
    }

    public void startManagedSession(TransactionIsolationLevel level) {
        localSqlSession.set(sqlSessionFactory.openSession(level));
    }

    public void startManagedSession(ExecutorType execType) {
        localSqlSession.set(sqlSessionFactory.openSession(execType));
    }

    public void startManagedSession(ExecutorType execType, boolean autoCommit) {
        localSqlSession.set(sqlSessionFactory.openSession(execType, autoCommit));
    }

    public void startManagedSession(ExecutorType execType, TransactionIsolationLevel level) {
        localSqlSession.set(sqlSessionFactory.openSession(execType, level));
    }

    public void startManagedSession(ExecutorType execType, Connection connection) {
        localSqlSession.set(sqlSessionFactory.openSession(execType, connection));
    }

    public boolean isManagedSessionStarted() {
        return localSqlSession.get() != null;
    }

    public Optional<SqlSession> currentManagedSession() {
        return Optional.ofNullable(localSqlSession.get());
    }

    public void closeManagedSession() {
        final SqlSession sqlSession = localSqlSession.get();
        if (sqlSession == null) {
            throw new SqlSessionException("Error:  Cannot close.  No managed session is started.");
        }
        try {
            sqlSession.close();
        } finally {
            localSqlSession.set(null);
        }
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
