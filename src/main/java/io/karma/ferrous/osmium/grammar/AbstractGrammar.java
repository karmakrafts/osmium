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

import io.karma.ferrous.osmium.grammar.node.Node;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public abstract class AbstractGrammar implements Grammar {
    protected final String name;
    protected final ArrayList<Node> nodes = new ArrayList<>();

    protected AbstractGrammar(final String name) {
        this.name = name;
    }

    public void addNodes(final List<? extends Node> nodes) {
        this.nodes.addAll(nodes);
    }

    public void addNode(final Node node) {
        nodes.add(node);
    }

    public void removeNode(final Node node) {
        nodes.remove(node);
    }

    @Override
    public List<? extends Grammar> getImports() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Node> getNodes() {
        return nodes;
    }

    @Override
    public String getName() {
        return name;
    }
}
