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

import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class LexerGrammar extends AbstractGrammar {
    private final ArrayList<Grammar> imports = new ArrayList<>();
    private boolean isResolved;

    public LexerGrammar(final String name) {
        super(name);
    }

    @Override
    public Grammar resolve() {
        if (isResolved) {
            return this;
        }
        for (final var _import : imports) { // First resolve all imported grammars
            _import.resolve();
        }
        final var nodes = this.nodes.values();
        for (final var node : nodes) {
            node.resolve(node, this.nodes);
        }
        isResolved = true;
        return this;
    }

    public void addImports(final List<LexerGrammar> imports) {
        this.imports.addAll(imports);
    }

    @Override
    public List<Grammar> getImports() {
        return imports;
    }

    @Override
    public GrammarType getType() {
        return GrammarType.LEXER;
    }
}
