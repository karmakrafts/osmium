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

import java.nio.file.Files;
import java.nio.file.Path;

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
            final var formatOpt = parser.accepts("f")
                .withRequiredArg()
                .ofType(String.class);
            final var inOpt = parser.accepts("i")
                .withRequiredArg()
                .ofType(String.class);
            final var outOpt = parser.accepts("o")
                .withRequiredArg()
                .ofType(String.class);
            final var configOpt = parser.accepts("c")
                .withOptionalArg()
                .ofType(String.class)
                .defaultsTo("osmium.json");
            // @formatter:on

            final var options = parser.parse(args);

            if (options.has(helpOpt)) {
                parser.printHelpOn(System.out);
                return; // Exit with code 0 by default
            }

            final var inPath = Path.of(options.valueOf(inOpt));
            if (!Files.exists(inPath) || Files.isDirectory(inPath)) {
                System.err.println("Input file does not exist or is not a file");
                System.exit(1);
            }

            final var outPath = Path.of(options.valueOf(outOpt));
            if (Files.exists(outPath)) {
                Files.delete(outPath);
            }

            final var configPath = Path.of(options.valueOf(configOpt));
            if (!Files.exists(configPath)) {
                System.err.println("Configuration file does not exist");
                System.exit(1);
            }

            final var config = TranspilerConfig.read(configPath);
            new Transpiler(config).transpile(inPath, outPath, options.valueOf(formatOpt));
        }
        catch (Throwable error) {
            System.err.println("Oops, that didn't quite work. Try running with -? to get some help");
            System.err.println(STR."\t\{error.fillInStackTrace()}");
            System.exit(1);
        }
    }

    private static final class NoArgsException extends RuntimeException {
        public NoArgsException() {
            super();
        }
    }
}
