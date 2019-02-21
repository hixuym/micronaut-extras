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
 * Abstract base implementation of composite {@link Specification} with default
 * implementations for {@code and}, {@code or} and {@code not}.
 * @author Michael
 * Created at: 2019/2/18 15:56
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

    /**
     * {@inheritDoc}
     */
    public abstract boolean isSatisfiedBy(T t);

    /**
     * {@inheritDoc}
     */
    public Specification<T> and(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    /**
     * {@inheritDoc}
     */
    public Specification<T> or(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

    /**
     * {@inheritDoc}
     */
    public Specification<T> not(final Specification<T> specification) {
        return new NotSpecification<T>(specification);
    }
}
