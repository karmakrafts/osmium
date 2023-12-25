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
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * @author Alexander Hinze
 * @since 25/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class RegexCompiler {
    // @formatter:off
    private RegexCompiler() {}
    // @formatter:on

    private static char escape(final char value) {
        return value;
    }

    private static String escape(final String value) {
        final var length = value.length();
        final var builder = new StringBuilder(length); // At least n chars
        for (var i = 0; i < length; i++) {
            builder.append(escape(value.charAt(i)));
        }
        return builder.toString();
    }

    private static void compileNode(final Node node, final StringBuilder builder, final @Nullable Node parent) {
        switch (node.getType()) {
            case LEXER_RULE, PARSER_RULE, FRAGMENT -> {
                for (final var child : node.getChildren()) {
                    if (child == parent) {
                        continue; // Prevent endless recursion
                    }
                    compileNode(child, builder, node);
                }
            }
            case ALT_LIST -> {
                final var count = node.getChildCount();
                final var children = node.getChildren();
                for (var i = 0; i < count; i++) {
                    final var child = children.get(i);
                    if (child == parent) {
                        continue; // Prevent endless recursion
                    }
                    compileNode(child, builder, node);
                    if (i > 0 && i < count - 1) {
                        builder.append('|');
                    }
                }
            }
            case ANY_MATCH -> builder.append('.');
            case TEXT -> builder.append(((TextNode) node).getText());
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
            case GROUP -> {
                builder.append('(');
                for (final var child : node.getChildren()) {
                    if (child == parent) {
                        continue; // Prevent endless recursion
                    }
                    compileNode(child, builder, node);
                }
                builder.append(')');
            }
            case UNARY_OP -> {
                final var unaryOpNode = (UnaryOpNode) node;
                final var unaryOp = unaryOpNode.getOp();
                final var child = unaryOpNode.getNode();
                if (child == parent) {
                    return;
                }
                if (unaryOp == UnaryOpNode.Op.MATCH_UNTIL) {
                    builder.append(STR."[\{unaryOp.getRegexPattern()}");
                    compileNode(child, builder, node);
                    builder.append(']');
                    return;
                }
                compileNode(child, builder, node);
                builder.append(unaryOp.getRegexPattern());
            }
        }
    }

    public static String compilePattern(final Node node) {
        final var builder = new StringBuilder();
        compileNode(node, builder, null);
        return builder.toString();
    }

    public static Pattern compile(final Node node) {
        return Pattern.compile(compilePattern(node));
    }
}
