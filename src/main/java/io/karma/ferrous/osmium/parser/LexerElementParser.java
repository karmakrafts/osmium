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

import io.karma.ferrous.antlr.ANTLRv4Parser.EbnfSuffixContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.LexerAtomContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.LexerElementContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.LexerElementsContext;
import io.karma.ferrous.osmium.grammar.node.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
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

    public static @Nullable Node parse(final Path parentDir, final @Nullable LexerElementContext context) {
        if (context == null) {
            return null;
        }
        final var parser = new LexerElementParser(parentDir);
        ParseTreeWalker.DEFAULT.walk(parser, context);
        return parser.node;
    }

    public static List<Node> parse(final Path parentDir, final @Nullable LexerElementsContext context) {
        if (context == null) {
            return Collections.emptyList();
        }
        // @formatter:off
        return context.lexerElement()
            .stream()
            .map(element -> parse(parentDir, element))
            .filter(Objects::nonNull)
            .toList();
        // @formatter:on
    }

    @Override
    public void enterLexerAtom(final LexerAtomContext context) {
        if (node != null) {
            return;
        }
        // Any match
        if (context.DOT() != null) {
            node = AnyMatchNode.INSTANCE;
            return;
        }
        // Ranges
        final var rangeContext = context.characterRange();
        if (rangeContext != null) {
            final var literals = rangeContext.STRING_LITERAL();
            final var start = literals.getFirst().getText().charAt(0);
            final var end = literals.getLast().getText().charAt(0);
            node = new RangeNode(start, end);
            return;
        }
        // Literal text and references
        final var terminalContext = context.terminalDef();
        if (terminalContext != null) {
            final var literal = terminalContext.STRING_LITERAL();
            if (literal != null) {
                final var rawText = literal.getText();
                node = new TextNode(rawText.substring(1, rawText.length() - 1));
            }
            else {
                node = new ReferenceNode(terminalContext.TOKEN_REF().getText());
            }
            return;
        }
        // Handle bracket blocks
        final var text = context.getText();
        if (text.startsWith("~")) {
            return; // TODO: handle match-until
        }
        if (!(text.startsWith("[")) || !text.endsWith("]")) {
            System.err.println("Could not parse character range");
            return;
        }
        final var pattern = text.substring(1, text.length() - 1); // Get rid of brackets
        node = new RawRangeNode(pattern);
    }

    @Override
    public void enterEbnfSuffix(final EbnfSuffixContext context) {
        if (node == null) {
            return;
        }
        node = new UnaryOpNode(parseUnaryOp(context), node);
    }
}
