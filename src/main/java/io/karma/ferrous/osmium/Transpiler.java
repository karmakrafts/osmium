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

import io.karma.ferrous.antlr.ANTLRv4Lexer;
import io.karma.ferrous.antlr.ANTLRv4Parser;
import io.karma.ferrous.antlr.ANTLRv4Parser.GrammarSpecContext;
import io.karma.ferrous.osmium.generator.Generator;
import io.karma.ferrous.osmium.generator.PygmentsGenerator;
import io.karma.ferrous.osmium.generator.TextMateGenerator;
import io.karma.ferrous.osmium.parser.ParserGrammarParser;
import io.karma.ferrous.osmium.util.DefaultErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Alexander Hinze
 * @since 13/12/2023
 */
@API(status = API.Status.STABLE)
public final class Transpiler {
    private final TranspilerConfig config;
    private final HashMap<String, Generator> generators = new HashMap<>();

    public Transpiler(final TranspilerConfig config) {
        this.config = config;
        addGenerator(new TextMateGenerator());
        addGenerator(new PygmentsGenerator());
    }

    public static GrammarSpecContext loadGrammar(final Path path) throws IOException {
        try (final var stream = Files.newInputStream(path); final var channel = Channels.newChannel(stream)) {
            final var charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8);
            final var lexer = new ANTLRv4Lexer(charStream);
            final var tokenStream = new CommonTokenStream(lexer);
            tokenStream.fill();
            final var parser = new ANTLRv4Parser(tokenStream);
            parser.removeErrorListeners();
            parser.addErrorListener(DefaultErrorListener.INSTANCE);
            return parser.grammarSpec();
        }
    }

    public TranspilerConfig getConfig() {
        return config;
    }

    public Collection<Generator> getGenerators() {
        return generators.values();
    }

    public void removeGenerator(final Generator generator) {
        final var name = generator.getName();
        if (!generators.containsKey(name)) {
            throw new IllegalStateException(STR."No generator named '\{name}'");
        }
        generators.remove(name);
    }

    public void addGenerator(final Generator generator) {
        final var name = generator.getName();
        if (generators.containsKey(name)) {
            throw new IllegalStateException(STR."Generator '\{name}' already registered");
        }
        generators.put(name, generator);
    }

    public @Nullable Generator getGenerator(final String name) {
        return generators.get(name);
    }

    public void transpile(final Path inPath, final Path outPath, final Generator generator) throws IOException {
        try (final var outStream = Files.newOutputStream(outPath); final var outChannel = Channels.newChannel(outStream)) {
            final var grammarContext = loadGrammar(inPath);
            final var grammar = ParserGrammarParser.parse(inPath.getParent(), grammarContext);
            if (grammar == null) {
                throw new IllegalStateException("Could not parse grammar");
            }
            generator.generate(outChannel, grammar, type -> generator.getTokenType(type, config));
        }
    }

    public void transpile(final Path inPath, final Path outPath, final String generator) throws IOException {
        transpile(inPath, outPath, getGenerator(generator));
    }
}
