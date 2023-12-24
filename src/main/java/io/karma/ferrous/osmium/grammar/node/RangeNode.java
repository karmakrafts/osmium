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

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import org.apiguardian.api.API;

/**
 * @author Alexander Hinze
 * @since 24/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class RangeNode implements Node {
    private final CharOpenHashSet chars;

    public RangeNode(final CharOpenHashSet chars) {
        this.chars = chars;
    }

    public CharOpenHashSet getChars() {
        return chars;
    }

    @Override
    public NodeType getType() {
        return NodeType.RANGE;
    }
}
