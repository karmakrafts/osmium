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

package io.karma.ferrous.osmium.grammar;

import io.karma.ferrous.osmium.grammar.node.GrammarNode;
import io.karma.ferrous.osmium.grammar.node.NamedNode;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public abstract class AbstractGrammar implements Grammar {
    protected final String name;
    protected final LinkedHashMap<String, NamedNode> nodes = new LinkedHashMap<>();
    protected final GrammarNode rootNode = new GrammarNode();

    protected AbstractGrammar(final String name) {
        this.name = name;
    }

    public boolean hasNode(final @Nullable String name) {
        if (name == null) {
            return false;
        }
        return nodes.containsKey(name);
    }

    public void addNodes(final List<? extends NamedNode> nodes) {
        for (final var node : nodes) {
            this.nodes.put(node.getName(), node);
        }
    }

    public void addNode(final NamedNode node) {
        nodes.put(node.getName(), node);
    }

    public void removeNode(final NamedNode node) {
        nodes.remove(node.getName());
    }

    public @Nullable NamedNode getNode(final String name) {
        return nodes.get(name);
    }

    @Override
    public List<Grammar> getImports() {
        return Collections.emptyList();
    }

    @Override
    public List<NamedNode> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    @Override
    public String getName() {
        return name;
    }
}
