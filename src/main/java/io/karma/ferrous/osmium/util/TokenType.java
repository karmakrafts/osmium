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

package io.karma.ferrous.osmium.util;

import org.apiguardian.api.API;

/**
 * @author Alexander Hinze
 * @since 23/12/2023
 */
@API(status = API.Status.STABLE)
public enum TokenType {
    // @formatter:off
    WHITESPACE,
    TEXT,
    COMMENT_INLINE,
    COMMENT_MULTILINE,
    COMMENT_DATA,
    PUNCTUATION,
    OPERATOR,
    OPERATOR_WORD,
    NUMBER_REAL,
    NUMBER_DEC,
    NUMBER_HEX,
    NUMBER_OCT,
    NUMBER_BIN,
    STRING,
    CHARACTER,
    KEYWORD,
    KEYWORD_CONSTANT,
    KEYWORD_DECL,
    KEYWORD_NAMESPACE,
    KEYWORD_TYPE,
    NAME,
    NAME_CLASS,
    NAME_FUNCTION,
    NAME_LABEL,
    NAME_NAMESPACE,
    NAME_VARIABLE,
    ILLEGAL
    // @formatter:on
}
