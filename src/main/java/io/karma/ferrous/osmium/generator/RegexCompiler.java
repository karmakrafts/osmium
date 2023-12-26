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

package io.karma.ferrous.osmium.generator;

import io.karma.ferrous.osmium.grammar.node.*;
import org.apiguardian.api.API;

import java.util.regex.Pattern;

/**
 * @author Alexander Hinze
 * @since 25/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class RegexCompiler {
    private static final char[] CHARS_TO_ESCAPE = ".?[](){}<>*+$/=|:-".toCharArray();

    // @formatter:off
    private RegexCompiler() {}
    // @formatter:on

    private static String sanitize(final char value) {
        for (final var c : CHARS_TO_ESCAPE) {
            if (c != value) {
                continue;
            }
            return STR."\\\{c}";
        }
        return Character.toString(value);
    }

    private static String sanitize(final String value) {
        final var length = value.length();
        final var builder = new StringBuilder(length); // At least n chars
        for (var i = 0; i < length; i++) {
            builder.append(sanitize(value.charAt(i)));
        }
        return builder.toString();
    }

    private static void compileNode(final Node node, final StringBuilder builder) {
        switch (node.getType()) {
            case GRAMMAR, LEXER_RULE, PARSER_RULE, SEQUENCE, FRAGMENT -> {
                for (final var child : node.getChildren()) {
                    compileNode(child, builder);
                }
            }
            case GROUP -> {
                builder.append('(');
                for (final var child : node.getChildren()) {
                    compileNode(child, builder);
                }
                builder.append(')');
            }
            case ALT_LIST -> {
                final var children = node.getVisibleChildren(); // We can't have empty alternatives in RegEx
                final var count = children.size();
                if (count > 1) {
                    builder.append('(');
                }
                for (var i = 0; i < count; i++) {
                    final var child = children.get(i);
                    compileNode(child, builder);
                    if (i < count - 1) {
                        builder.append('|');
                    }
                }
                if (count > 1) {
                    builder.append(')');
                }
            }
            case ANY_MATCH -> builder.append('.');
            case TEXT -> builder.append(sanitize(((TextNode) node).getText()));
            case RANGE -> {
                final var range = (RangeNode) node;
                // @formatter:off
                builder.append('[')
                    .append(range.getStart())
                    .append('-')
                    .append(range.getEnd())
                    .append(']');
                // @formatter:on
            }
            case RAW_RANGE -> {
                final var range = (RawRangeNode) node;
                // @formatter:off
                builder.append('[')
                    .append(range.getPattern())
                    .append(']');
                // @formatter:on
            }
            case UNARY_OP -> {
                final var unaryOpNode = (UnaryOpNode) node;
                final var unaryOp = unaryOpNode.getOp();
                final var child = unaryOpNode.getNode();
                if (child.getType() == NodeType.UNARY_OP) {
                    compileNode(child, builder);
                    return;
                }
                if (unaryOp == UnaryOpNode.Op.MATCH_UNTIL) {
                    builder.append(STR."[\{unaryOp.getRegexPattern()}");
                    compileNode(child, builder);
                    builder.append(']');
                    return;
                }
                final var isGroup = child.getVisibleChildCount() > 1;
                if (isGroup) {
                    builder.append('(');
                }
                compileNode(child, builder);
                if (isGroup) {
                    builder.append(')');
                }
                builder.append(unaryOp.getRegexPattern());
            }
        }
    }

    public static String compilePattern(final Node node) {
        final var builder = new StringBuilder();
        compileNode(node, builder);
        return builder.toString();
    }

    public static Pattern compile(final Node node) {
        return Pattern.compile(compilePattern(node));
    }
}
