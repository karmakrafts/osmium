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

package io.karma.ferrous.osmium.parser;

import io.karma.ferrous.antlr.ANTLRv4Parser.GrammarDeclContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.OptionContext;
import io.karma.ferrous.osmium.grammar.Grammar;
import io.karma.ferrous.osmium.grammar.ParserGrammar;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class ParserGrammarParser extends ParseAdapter {
    private ParserGrammar grammar;

    private ParserGrammarParser(final Path parentDir) {
        super(parentDir);
    }

    public static @Nullable Grammar parse(final Path parentDir, final @Nullable ParseTree context) {
        if (context == null) {
            return null;
        }
        final var parser = new ParserGrammarParser(parentDir);
        ParseTreeWalker.DEFAULT.walk(parser, context);
        return parser.grammar.resolve();
    }

    @Override
    public void enterGrammarDecl(final GrammarDeclContext context) {
        if (context.grammarType().PARSER() == null) {
            throw new IllegalStateException("Grammar is not a parser grammar");
        }
        grammar = new ParserGrammar(context.identifier().getText());
    }

    @Override
    public void enterOption(final OptionContext context) {
        final var name = context.identifier().getText();
        final var value = context.optionValue();
        switch (name) {
            case "tokenVocab" -> grammar.setLexerGrammar(loadLexerGrammar(value.getText()));
            // Add more parser options here
        }
    }
}
