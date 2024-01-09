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

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Alexander Hinze
 * @since 27/12/2023
 */
public interface ContainerNode extends Node {
    @Override
    default boolean isContainer() {
        return true;
    }

    @Override
    default void compileRegex(final StringBuilder builder) {
        final var children = getChildren();
        final var isGroup = children.size() > 1;
        if (isGroup) {
            builder.append('(');
        }
        for (final var child : children) {
            child.compileRegex(builder);
        }
        if (isGroup) {
            builder.append(')');
        }
    }

    default void resolve(final @Nullable NamedNode rootNode, final Map<String, NamedNode> nodes) {
        final var children = getChildren();
        for (final var child : children) {
            if (!(child instanceof ContainerNode container)) {
                continue;
            }
            container.resolve(rootNode, nodes); // Resolve from inside out
        }
        final var count = children.size();
        for (var i = 0; i < count; i++) {
            final var child = children.get(i);
            if (!child.getType().isReference()) {
                continue;
            }
            final var refName = ((ReferenceNode) child).getName();
            final var refNode = nodes.get(refName);
            if (refNode == null) {
                continue;
            }
            if (refNode == rootNode) { // Substitute self-ref node to prevent infinite recursion in compiler
                setChild(i, new SelfReferenceNode());
                continue;
            }
            setChild(i, refNode);
        }
    }

    Node removeChild(final int index);

    void setChild(final int index, final Node child);

    List<Node> getChildren();

    default int getChildCount() {
        return getChildren().size();
    }
}
