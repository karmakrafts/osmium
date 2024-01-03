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

import io.karma.ferrous.antlr.ANTLRv4Parser;
import io.karma.ferrous.antlr.ANTLRv4Parser.LexerAltListContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.LexerElementContext;
import io.karma.ferrous.osmium.grammar.node.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexander Hinze
 * @since 25/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class LexerElementParser extends ParseAdapter {
    private Node node;

    public LexerElementParser(final Path parentDir) {
        super(parentDir);
    }

    public static @Nullable Node parse(final Path parentDir, final @Nullable ParseTree context) {
        if (context == null) {
            return null;
        }
        final var parser = new LexerElementParser(parentDir);
        ParseTreeWalker.DEFAULT.walk(parser, context);
        return parser.node;
    }

    public static List<Node> parseAll(final Path parentDir, final @Nullable ParserRuleContext context) {
        if (context == null) {
            return Collections.emptyList();
        }
        // @formatter:off
        return context.children
            .stream()
            .map(element -> parse(parentDir, element))
            .filter(Objects::nonNull)
            .toList();
        // @formatter:on
    }

    private static @Nullable Node parseSetElement(final @Nullable ANTLRv4Parser.SetElementContext context) {
        if (context == null) {
            return null;
        }
        final var refContext = context.TOKEN_REF();
        if (refContext != null) {
            return new ReferenceNode(refContext.getText());
        }
        final var literalContext = context.STRING_LITERAL();
        if (literalContext != null) {
            final var text = literalContext.getText();
            return new TextNode(text.substring(1, text.length() - 1));
        }
        return parseRawRange(context);
    }

    private static @Nullable Node parseRawRange(final @Nullable ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        final var text = context.getText();
        if (!text.startsWith("[") || !text.contains("]")) {
            return null;
        }
        return new RawRangeNode(text.substring(1, text.lastIndexOf(']')));
    }

    private static @Nullable Node parseAtom(final @Nullable ANTLRv4Parser.LexerAtomContext context) {
        if (context == null) {
            return null;
        }
        // Any match
        if (context.DOT() != null) {
            return new AnyMatchNode();
        }
        // Ranges
        final var rangeContext = context.characterRange();
        if (rangeContext != null) {
            final var literals = rangeContext.STRING_LITERAL();
            final var start = literals.getFirst().getText().charAt(0);
            final var end = literals.getLast().getText().charAt(0);
            return new RangeNode(start, end);
        }
        // Literal text and references
        final var terminalContext = context.terminalDef();
        if (terminalContext != null) {
            final var literal = terminalContext.STRING_LITERAL();
            if (literal != null) {
                final var rawText = literal.getText();
                return new TextNode(rawText.substring(1, rawText.length() - 1));
            }
            else {
                return new ReferenceNode(terminalContext.TOKEN_REF().getText());
            }
        }
        final var notSetContext = context.notSet();
        if (notSetContext != null) {
            final var blockSetContext = notSetContext.blockSet();
            if (blockSetContext != null) {
                final var elements = new ArrayList<Node>();
                final var elementContexts = blockSetContext.setElement();
                for (final var elementContext : elementContexts) {
                    final var element = parseSetElement(elementContext);
                    if (element == null) {
                        System.err.println(STR."Could not parse not-set element: \{elementContext.getText()}");
                        continue;
                    }
                    elements.add(element);
                }
                return new NotSetNode(elements);
            }
            else {
                final var elementContext = notSetContext.setElement();
                if (elementContext == null) {
                    System.err.println(STR."Could not parse not-set: \{context.getText()}");
                    return null;
                }
                final var element = parseSetElement(elementContext);
                if (element == null) {
                    System.err.println(STR."Could not parse not-set element: \{elementContext.getText()}");
                    return null;
                }
                return new NotSetNode(Collections.singletonList(element));
            }
        }
        return parseRawRange(context);
    }

    @Override
    public void enterLexerAltList(final LexerAltListContext context) {
        if (node != null) {
            return;
        }
        final var altContexts = context.lexerAlt();
        final var elements = new ArrayList<Node>();
        for (final var altContext : altContexts) {
            final var alts = parseAll(parentDir, altContext.lexerElements());
            if (alts.size() == 1) {
                elements.add(alts.getFirst());
                continue;
            }
            elements.add(new SequenceNode(alts));
        }
        node = new AltListNode(elements);
    }

    @Override
    public void enterLexerElement(final LexerElementContext context) {
        if (node != null) {
            return;
        }
        final var blockContext = context.lexerBlock();
        if (blockContext != null) {
            node = parse(parentDir, blockContext);
        }
        final var atomContext = context.lexerAtom();
        if (atomContext != null) {
            node = parseAtom(atomContext);
        }
        if (node == null) {
            System.err.println(STR."Could not parse node: \{context.getText()}");
            return;
        }
        final var suffixContext = context.ebnfSuffix();
        if (suffixContext != null) {
            final var op = parseUnaryOp(suffixContext);
            node = new UnaryOpNode(op, node);
        }
    }
}
