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
 * @since 24/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class GroupNode extends AbstractContainerNode<Node> {
    private final boolean isRightAssoc;

    public GroupNode(final List<Node> children, final boolean isRightAssoc) {
        super(children);
        this.isRightAssoc = isRightAssoc;
    }

    public boolean isRightAssoc() {
        return isRightAssoc;
    }

    @Override
    public NodeType getType() {
        return NodeType.GROUP;
    }
}