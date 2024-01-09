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

package io.karma.ferrous.osmium.generator;

import io.karma.ferrous.osmium.TranspilerConfig;
import io.karma.ferrous.osmium.grammar.Grammar;
import io.karma.ferrous.osmium.grammar.ParserGrammar;
import io.karma.ferrous.osmium.grammar.node.NamedNode;
import io.karma.ferrous.osmium.util.TokenType;
import org.apiguardian.api.API;

import java.nio.channels.WritableByteChannel;
import java.util.EnumMap;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class TextMateGenerator implements Generator {
    public static final String NAME = "textmate";
    private static final EnumMap<TokenType, String> TOKEN_TYPES = new EnumMap<>(TokenType.class);

    static {
        // Implemented according to https://macromates.com/manual/en/language_grammars
        // @formatter:off
        TOKEN_TYPES.put(TokenType.WHITESPACE,        "support.other");
        TOKEN_TYPES.put(TokenType.TEXT,              "markup.raw");
        TOKEN_TYPES.put(TokenType.COMMENT_INLINE,    "comment.line");
        TOKEN_TYPES.put(TokenType.COMMENT_MULTILINE, "comment.block");
        TOKEN_TYPES.put(TokenType.COMMENT_DATA,      "comment.block.documentation");
        TOKEN_TYPES.put(TokenType.PUNCTUATION,       "punctuation");
        TOKEN_TYPES.put(TokenType.OPERATOR,          "keyword.operator");
        TOKEN_TYPES.put(TokenType.OPERATOR_WORD,     "keyword.operator.word");
        TOKEN_TYPES.put(TokenType.NUMBER_REAL,       "constant.numeric.real");
        TOKEN_TYPES.put(TokenType.NUMBER_DEC,        "constant.numeric.real");
        TOKEN_TYPES.put(TokenType.NUMBER_HEX,        "constant.numeric.hex");
        TOKEN_TYPES.put(TokenType.NUMBER_OCT,        "constant.numeric.oct");
        TOKEN_TYPES.put(TokenType.NUMBER_BIN,        "constant.numeric.bin");
        TOKEN_TYPES.put(TokenType.STRING,            "string.quoted");
        TOKEN_TYPES.put(TokenType.CHARACTER,         "constant.character");
        TOKEN_TYPES.put(TokenType.KEYWORD,           "keyword");
        TOKEN_TYPES.put(TokenType.KEYWORD_CONSTANT,  "keyword");
        TOKEN_TYPES.put(TokenType.KEYWORD_DECL,      "keyword");
        TOKEN_TYPES.put(TokenType.KEYWORD_NAMESPACE, "keyword");
        TOKEN_TYPES.put(TokenType.KEYWORD_TYPE,      "keyword");
        TOKEN_TYPES.put(TokenType.NAME,              "entity.name");
        TOKEN_TYPES.put(TokenType.NAME_CLASS,        "entity.name.type");
        TOKEN_TYPES.put(TokenType.NAME_FUNCTION,     "entity.name.function");
        TOKEN_TYPES.put(TokenType.NAME_LABEL,        "entity.name");
        TOKEN_TYPES.put(TokenType.NAME_NAMESPACE,    "entity.name");
        TOKEN_TYPES.put(TokenType.NAME_VARIABLE,     "entity.name");
        TOKEN_TYPES.put(TokenType.ILLEGAL,           "invalid.illegal");
        // @formatter:on
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void generate(final WritableByteChannel channel, final Grammar grammar, final TranspilerConfig config) {
        if (!(grammar instanceof ParserGrammar parserGrammar)) {
            return;
        }
        for (final NamedNode node : parserGrammar.getNodes()) {

        }
    }
}
