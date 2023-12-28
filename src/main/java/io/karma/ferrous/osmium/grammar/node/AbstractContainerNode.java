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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Hinze
 * @since 27/12/2023
 */
@API(status = API.Status.INTERNAL)
public abstract class AbstractContainerNode extends AbstractNode implements ContainerNode {
    protected final ArrayList<Node> children = new ArrayList<>();

    public void addChildren(final List<Node> children) {
        this.children.addAll(children);
    }

    public void addChild(final Node child) {
        children.add(child);
    }

    @Override
    public Node removeChild(final int index) {
        return children.remove(index);
    }

    @Override
    public void setChild(final int index, final Node child) {
        children.set(index, child);
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public int getChildCount() {
        return children.size();
    }
}
