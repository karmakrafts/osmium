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

package io.karma.ferrous.osmium;

import joptsimple.OptionParser;
import org.apiguardian.api.API;

/**
 * @author Alexander Hinze
 * @since 12/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class Main {
    public static void main(final String[] args) {
        try {
            if (args.length == 0) {
                throw new NoArgsException();
            }

            final var parser = new OptionParser(false);

            // @formatter:off
            final var helpOpt = parser.accepts("?");
            final var inOpt = parser.accepts("i")
                .withRequiredArg()
                .ofType(String.class);
            final var outOpt = parser.accepts("o")
                .withOptionalArg()
                .ofType(String.class);
            // @formatter:on
        }
        catch (Throwable error) {
            System.err.println(error.getMessage()); // TODO: improve error handling
        }
    }

    private static final class NoArgsException extends RuntimeException {
        public NoArgsException() {
            super();
        }
    }
}
