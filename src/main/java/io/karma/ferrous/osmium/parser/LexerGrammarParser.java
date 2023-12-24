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
import io.karma.ferrous.osmium.grammar.node.*;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;

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
        System.out.println(STR."Found lexer rule/fragment: \{name}");
        final var altContexts = context.lexerRuleBlock().lexerAltList().lexerAlt();
        final var children = new ArrayList<Node>();
        for (final var altContext : altContexts) {
            final var elementContexts = altContext.lexerElements().lexerElement();
            for (final var elementContext : elementContexts) {
                final var atomContext = elementContext.lexerAtom();
                final var unaryOp = parseUnaryOp(elementContext.ebnfSuffix());
                Node child = null;
                if (atomContext != null) {
                    // Ranges
                    final var rangeContext = atomContext.characterRange();
                    if (rangeContext != null) {
                        final var literals = rangeContext.STRING_LITERAL();
                        final var start = literals.getFirst().getText().charAt(0);
                        final var end = literals.getLast().getText().charAt(0);
                        final var chars = new CharOpenHashSet();
                        for (var c = start; c <= end; c++) {
                            chars.add(c);
                        }
                        child = new RangeNode(chars);
                    }
                    // Literal text and references
                    final var terminalContext = atomContext.terminalDef();
                    if (terminalContext != null) {
                        final var literal = terminalContext.STRING_LITERAL();
                        if (literal != null) {
                            child = new TextNode(literal.getText());
                        }
                        else {
                            child = new ReferenceNode(terminalContext.TOKEN_REF().getText());
                        }
                    }
                }
                if (child == null) {
                    continue; // TODO: handle error
                }
                if (unaryOp != null) {
                    children.add(new UnaryOpNode(unaryOp, child));
                    continue;
                }
                children.add(child);
            }
        }
        if (context.FRAGMENT() != null) { // We are parsing a fragment
            grammar.addNode(new FragmentNode(name, children));
            return;
        }
        grammar.addNode(new LexerRuleNode(name, children));
    }
}
