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

import io.karma.ferrous.antlr.ANTLRv4Parser.DelegateGrammarsContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.GrammarDeclContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.LexerRuleSpecContext;
import io.karma.ferrous.osmium.grammar.LexerGrammar;
import io.karma.ferrous.osmium.grammar.node.FragmentNode;
import io.karma.ferrous.osmium.grammar.node.LexerRuleNode;
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
public final class LexerGrammarParser extends ParseAdapter {
    private LexerGrammar grammar;

    private LexerGrammarParser(final Path parentDir) {
        super(parentDir);
    }

    public static @Nullable LexerGrammar parse(final Path parentDir, final @Nullable ParseTree context) {
        if (context == null) {
            return null;
        }
        final var parser = new LexerGrammarParser(parentDir);
        ParseTreeWalker.DEFAULT.walk(parser, context);
        return parser.grammar;
    }

    @Override
    public void enterGrammarDecl(final GrammarDeclContext context) {
        if (context.grammarType().LEXER() == null) {
            throw new IllegalStateException("Grammar is not a lexer grammar");
        }
        grammar = new LexerGrammar(context.identifier().getText());
    }

    @Override
    public void enterDelegateGrammars(final DelegateGrammarsContext context) {
        // @formatter:off
        grammar.addImports(context.delegateGrammar()
            .stream()
            .map(importContext -> loadLexerGrammar(importContext.identifier().getFirst().getText()))
            .toList());
        // @formatter:on
    }

    @Override
    public void enterLexerRuleSpec(final LexerRuleSpecContext context) {
        final var name = context.TOKEN_REF().getText();
        if (grammar.hasNode(name)) {
            System.err.println(STR."Found duplicated rule '\{name}' in lexer grammar");
            return;
        }
        final var altListContext = context.lexerRuleBlock().lexerAltList();
        final var altList = LexerElementParser.parse(parentDir, altListContext);
        if (context.FRAGMENT() != null) { // We are parsing a fragment
            final var fragment = new FragmentNode(name);
            fragment.addChild(altList);
            grammar.addNode(fragment);
            return;
        }
        final var rule = new LexerRuleNode(name);
        rule.addChild(altList);
        grammar.addNode(rule);
    }
}
