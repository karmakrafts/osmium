/*
 * Copyright 2023 Karma Krafts & associates
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
 */

package io.karma.ferrous.osmium.util;

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import org.apiguardian.api.API;

/**
 * @author Alexander Hinze
 * @since 28/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class RegexUtils {
    private static final CharOpenHashSet ESCAPED_CHARS = new CharOpenHashSet("-+*/()[]{}|^$.:".toCharArray());

    // @formatter:off
    private RegexUtils() {}
    // @formatter:on

    public static String escape(final char value) {
        if (!ESCAPED_CHARS.contains(value)) {
            return Character.toString(value);
        }
        return STR."\\\{value}";
    }

    public static String escape(final String value) {
        final var length = value.length();
        final var builder = new StringBuilder();
        for (var i = 0; i < length; i++) {
            builder.append(escape(value.charAt(i)));
        }
        return builder.toString();
    }
}
