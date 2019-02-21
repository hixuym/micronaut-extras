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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public final class Lists {
    private Lists() {
    }

    public static <T> List<T> of(Iterable<T> elements) {
        final List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return unmodifiableList(list);
    }

    public static <T> List<T> of(Iterator<T> it) {
        final List<T> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return unmodifiableList(list);
    }
}
