/*
 * Copyright 2024 Karma Krafts & associates
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

import io.karma.ferrous.antlr.ANTLRv4Parser.AtomContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.ElementContext;
import io.karma.ferrous.antlr.ANTLRv4Parser.RuleAltListContext;
import io.karma.ferrous.osmium.grammar.node.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Alexander Hinze
 * @since 03/01/2024
 */
@API(status = API.Status.INTERNAL)
public final class ParserElementParser extends ParseAdapter {
    private Node node;

    public ParserElementParser(final Path parentDir) {
        super(parentDir);
    }

    public static @Nullable Node parse(final Path parentDir, final @Nullable ParserRuleContext context) {
        final var parser = new ParserElementParser(parentDir);
        ParseTreeWalker.DEFAULT.walk(parser, context);
        return parser.node;
    }

    private static @Nullable Node parseAtom(final @Nullable AtomContext context) {
        if (context == null) {
            return null;
        }
        // Any match
        if (context.DOT() != null) {
            return new AnyMatchNode();
        }
        // Rule references
        final var ruleRefContext = context.ruleref();
        if (ruleRefContext != null) {
            return new ReferenceNode(ruleRefContext.RULE_REF().getText(), true);
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
                return new ReferenceNode(terminalContext.TOKEN_REF().getText(), false);
            }
        }
        // Not-sets
        final var notSetContext = context.notSet();
        if (notSetContext != null) {
            final var blockSetContext = notSetContext.blockSet();
            if (blockSetContext != null) {
                return parseBlockSet(blockSetContext);
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
    public void enterRuleAltList(final RuleAltListContext context) {
        if (node != null) {
            return;
        }
        final var altContexts = context.labeledAlt();
        final var alts = new ArrayList<Node>();
        for (final var altContext : altContexts) {
            final var alt = parse(parentDir, altContext);
            if (alt == null) {
                System.err.println(STR."Could not parse parser rule: \{context.getText()}");
                continue;
            }
            alts.add(alt);
        }
        node = new AltListNode(alts);
    }

    @Override
    public void enterElement(final ElementContext context) {
        if (node != null) {
            return;
        }
        final var ebnfContext = context.ebnf();
        if (ebnfContext != null) {
            node = parse(parentDir, ebnfContext.block());
            final var suffixContext = ebnfContext.blockSuffix();
            if (suffixContext != null) {
                final var op = parseUnaryOp(suffixContext.ebnfSuffix());
                node = new UnaryOpNode(op, node);
            }
        }
        final var atomContext = context.atom();
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
