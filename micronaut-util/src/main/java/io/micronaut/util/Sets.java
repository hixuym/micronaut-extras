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

package io.micronaut.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public final class Sets {
    private Sets() {
    }

    public static <T> Set<T> of(T e1, T e2) {
        final Set<T> set = new HashSet<>(2);
        set.add(e1);
        set.add(e2);
        return unmodifiableSet(set);
    }

    public static <T> Set<T> of(T e1, T e2, T e3) {
        final Set<T> set = new HashSet<>(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return unmodifiableSet(set);
    }

    public static <T> Set<T> of(T e1, T e2, T e3, T e4) {
        final Set<T> set = new HashSet<>(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return unmodifiableSet(set);
    }

    public static <T> Set<T> of(T e1, T e2, T e3, T e4, T e5) {
        final Set<T> set = new HashSet<>(5);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        return unmodifiableSet(set);
    }

    @SafeVarargs
    public static <T> Set<T> of(T... elements) {
        final Set<T> set = new HashSet<>(elements.length);
        set.addAll(Arrays.asList(elements));
        return unmodifiableSet(set);
    }

    public static <T> Set<T> of(Iterable<T> elements) {
        final Set<T> set = new HashSet<>();
        for (T element : elements) {
            set.add(element);
        }
        return unmodifiableSet(set);
    }
}
