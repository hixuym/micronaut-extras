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

import javax.annotation.Nullable;

/**
 * Helper methods for enum types.
 */
public class Enums {

    /**
     * Convert a string to an enum with more permissive rules than {@link Enum} valueOf().
     * This method is more permissive in the following ways:
     * <ul>
     * <li>Whitespace is permitted but stripped from the input.</li>
     * <li>Dashes and periods in the value are converted to underscores.</li>
     * <li>Matching against the enum values is case insensitive.</li>
     * </ul>
     * @param value The string to convert.
     * @param constants The list of constants for the {@link Enum} to which you wish to convert.
     * @return The enum or null, if no enum constant matched the input value.
     */
    @Nullable
    public static Enum<?> fromStringFuzzy(String value, Enum<?>[] constants) {
        final String text = value
                .replace(" ", "")
                .replace("\n", "")
                .replace("\r", "")
                .replace("\t", "")
                .replace('-', '_')
                .replace('.', '_');
        for (Enum<?> constant : constants) {
            if (constant.name().equalsIgnoreCase(text)) {
                return constant;
            }
        }

        // In some cases there are certain enums that don't follow the same pattern across an enterprise.  So this
        // means that you have a mix of enums that use toString(), some use @JsonCreator, and some just use the
        // standard constant name().  This block handles finding the proper enum by toString()
        for (Enum<?> constant : constants) {
            if (constant.toString().equalsIgnoreCase(value)) {
                return constant;
            }
        }

        return null;
    }

}
