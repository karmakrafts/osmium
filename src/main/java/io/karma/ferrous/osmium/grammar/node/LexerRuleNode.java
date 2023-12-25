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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Hinze
 * @since 24/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class LexerRuleNode implements NamedNode {
    private final String name;
    private final ArrayList<Node> children = new ArrayList<>();
    private Node parent;

    public LexerRuleNode(final String name, final List<Node> children) {
        this.name = name;
        this.children.addAll(children);
        for (final var child : children) {
            child.setParent(this);
        }
    }

    @Override
    public @Nullable Node getParent() {
        return parent;
    }

    @Override
    public void setParent(final @Nullable Node parent) {
        this.parent = parent;
    }

    @Override
    public void setChild(final int index, final Node child) {
        children.set(index, child);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public List<? extends Node> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NodeType getType() {
        return NodeType.LEXER_RULE;
    }
}
