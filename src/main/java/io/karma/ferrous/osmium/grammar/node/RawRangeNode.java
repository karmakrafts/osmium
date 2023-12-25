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

/**
 * @author Alexander Hinze
 * @since 25/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class RawRangeNode implements Node {
    private final String pattern;
    private Node parent;

    public RawRangeNode(final String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
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
    public NodeType getType() {
        return NodeType.RAW_RANGE;
    }
}
