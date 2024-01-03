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

package io.karma.ferrous.osmium.grammar.node;

import org.apiguardian.api.API;

import java.util.List;

/**
 * @author Alexander Hinze
 * @since 28/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class NotSetNode extends AbstractContainerNode {
    public NotSetNode() {
    }

    public NotSetNode(final List<Node> children) {
        addChildren(children);
    }

    private static String fillRange(final RangeNode node) {
        final var builder = new StringBuilder();
        for (var c = node.getStart(); c <= node.getEnd(); c++) {
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    public void compileRegex(final StringBuilder builder) {
        builder.append("[^");
        for (final var child : getChildren()) {
            if (child.getType().isRange()) {
                switch (child) {
                    case RangeNode range -> builder.append(fillRange(range));
                    case RawRangeNode range -> builder.append(range.getPattern());
                    default -> {
                    }
                }
                continue;
            }
            child.compileRegex(builder);
        }
        builder.append(']');
    }

    @Override
    public NodeType getType() {
        return NodeType.NOT_SET;
    }
}
