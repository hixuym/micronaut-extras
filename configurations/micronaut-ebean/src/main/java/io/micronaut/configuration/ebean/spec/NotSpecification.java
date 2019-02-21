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

package io.micronaut.configuration.ebean.spec;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (NOT) of the given spec.
 * @author Michael
 * Created at: 2019/2/18 15:57
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;

    /**
     * Create a new NOT specification based on another spec.
     *
     * @param spec1 Specification instance to not.
     */
    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSatisfiedBy(final T t) {
        return !spec1.isSatisfiedBy(t);
    }
}
