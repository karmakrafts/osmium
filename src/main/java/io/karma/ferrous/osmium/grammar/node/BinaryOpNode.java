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

import java.util.Arrays;

/**
 * @author Alexander Hinze
 * @since 24/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class BinaryOpNode extends AbstractContainerNode<Node> {
    private final Op op;
    private final Node lhs;
    private final Node rhs;

    public BinaryOpNode(final Op op, final Node lhs, final Node rhs) {
        super(Arrays.asList(lhs, rhs));
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Op getOp() {
        return op;
    }

    public Node getLhs() {
        return lhs;
    }

    public Node getRhs() {
        return rhs;
    }

    @Override
    public NodeType getType() {
        return NodeType.BINARY_OP;
    }

    public enum Op {
        DISJUNCTION
    }
}