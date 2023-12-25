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

import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Hinze
 * @since 24/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class UnaryOpNode implements Node {
    private final Op op;
    private Node node;

    public UnaryOpNode(final Op op, final Node node) {
        this.op = op;
        this.node = node;
    }

    public Op getOp() {
        return op;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public void setChild(int index, Node child) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        node = child;
    }

    @Override
    public int getChildCount() {
        return 1;
    }

    @Override
    public List<? extends Node> getChildren() {
        return Collections.singletonList(node);
    }

    @Override
    public NodeType getType() {
        return NodeType.UNARY_OP;
    }

    public enum Op {
        // @formatter:off
        ZERO_OR_MORE("*"),
        ONE_OR_MORE ("+"),
        ZERO_OR_ONE ("?"),
        MATCH_UNTIL ("^");
        // @formatter:on

        private final String regexPattern;

        Op(final String regexPattern) {
            this.regexPattern = regexPattern;
        }

        public String getRegexPattern() {
            return regexPattern;
        }
    }
}
