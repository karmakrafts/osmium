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
import io.karma.ferrous.osmium.util.TokenType;
import org.apiguardian.api.API;

import java.nio.channels.WritableByteChannel;
import java.util.EnumMap;
import java.util.function.Function;

/**
 * @author Alexander Hinze
 * @since 20/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class PygmentsGenerator implements Generator {
    public static final String NAME = "pygments";
    private static final EnumMap<TokenType, String> TOKEN_TYPES = new EnumMap<>(TokenType.class);

    static {
        // Implemented according to https://pygments.org/docs/tokens/
        // @formatter:off
        TOKEN_TYPES.put(TokenType.WHITESPACE,        "Whitespace");
        TOKEN_TYPES.put(TokenType.TEXT,              "Text");
        TOKEN_TYPES.put(TokenType.COMMENT_INLINE,    "Comment.Single");
        TOKEN_TYPES.put(TokenType.COMMENT_MULTILINE, "Comment.Multiline");
        TOKEN_TYPES.put(TokenType.COMMENT_DATA,      "Comment.Special");
        TOKEN_TYPES.put(TokenType.PUNCTUATION,       "Punctuation");
        TOKEN_TYPES.put(TokenType.OPERATOR,          "Operator");
        TOKEN_TYPES.put(TokenType.OPERATOR_WORD,     "Operator.Word");
        TOKEN_TYPES.put(TokenType.NUMBER_REAL,       "Number.Float");
        TOKEN_TYPES.put(TokenType.NUMBER_DEC,        "Number.Integer");
        TOKEN_TYPES.put(TokenType.NUMBER_HEX,        "Number.Hex");
        TOKEN_TYPES.put(TokenType.NUMBER_OCT,        "Number.Oct");
        TOKEN_TYPES.put(TokenType.NUMBER_BIN,        "Number.Bin");
        TOKEN_TYPES.put(TokenType.STRING,            "String");
        TOKEN_TYPES.put(TokenType.CHARACTER,         "String.Char");
        TOKEN_TYPES.put(TokenType.KEYWORD,           "Keyword");
        TOKEN_TYPES.put(TokenType.KEYWORD_CONSTANT,  "Keyword.Constant");
        TOKEN_TYPES.put(TokenType.KEYWORD_DECL,      "Keyword.Declaration");
        TOKEN_TYPES.put(TokenType.KEYWORD_NAMESPACE, "Keyword.Namespace");
        TOKEN_TYPES.put(TokenType.KEYWORD_TYPE,      "Keyword.Type");
        TOKEN_TYPES.put(TokenType.NAME,              "Name");
        TOKEN_TYPES.put(TokenType.NAME_CLASS,        "Name.Class");
        TOKEN_TYPES.put(TokenType.NAME_FUNCTION,     "Name.Function");
        TOKEN_TYPES.put(TokenType.NAME_LABEL,        "Name.Label");
        TOKEN_TYPES.put(TokenType.NAME_NAMESPACE,    "Name.Namespace");
        TOKEN_TYPES.put(TokenType.NAME_VARIABLE,     "Name.Variable");
        TOKEN_TYPES.put(TokenType.ILLEGAL,           "Error");
        // @formatter:on
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void generate(final WritableByteChannel channel, final Grammar grammar,
                         final Function<TokenType, String> tokenTransformer) {

    }

    @Override
    public String getTokenType(final TokenType type, final TranspilerConfig config) {
        return TOKEN_TYPES.get(type);
    }
}
