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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Hinze
 * @since 24/12/2023
 */
@API(status = API.Status.INTERNAL)
public interface Node {
    NodeType getType();

    default @Nullable Node getParent() {
        return null;
    }

    default void setParent(final @Nullable Node parent) {
    }

    default void resolve(final NamedNode rootNode, final Map<String, NamedNode> nodes) {
        final var children = getChildren();
        for (final var child : children) {
            child.resolve(rootNode, nodes); // Resolve from inside out
        }
        final var count = children.size();
        for (var i = 0; i < count; i++) {
            final var child = children.get(i);
            if (child.getType() != NodeType.REFERENCE) {
                continue;
            }
            final var refName = ((ReferenceNode) child).getName();
            final var refNode = nodes.get(refName);
            if (refNode == null) {
                continue;
            }
            if (refNode == rootNode) {
                setChild(i, new SelfReferenceNode());
                continue;
            }
            setChild(i, refNode);
        }
    }

    default void setChild(final int index, final Node child) {
        throw new UnsupportedOperationException();
    }

    default List<? extends Node> getChildren() {
        return Collections.emptyList();
    }

    default int getChildCount() {
        return getChildren().size();
    }

    default boolean isNamed() {
        return false;
    }
}
