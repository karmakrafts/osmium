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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apiguardian.api.API;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author Alexander Hinze
 * @since 21/12/2023
 */
@API(status = API.Status.INTERNAL)
public final class JSONUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectReader READER = MAPPER.reader();
    private static final ObjectWriter WRITER = MAPPER.writer();

    // @formatter:off
    private JSONUtils() {}
    // @formatter:on

    @SuppressWarnings("unchecked")
    public static <N extends JsonNode> N read(final ReadableByteChannel channel) throws IOException {
        try (final var reader = Channels.newReader(channel, StandardCharsets.UTF_8)) {
            return (N) READER.readTree(reader);
        }
    }

    public static <T> T readValue(final Class<T> type, final ReadableByteChannel channel) throws IOException {
        try (final var reader = Channels.newReader(channel, StandardCharsets.UTF_8)) {
            return READER.readValue(reader, type);
        }
    }

    public static void writeValue(final Object value, final WritableByteChannel channel) throws IOException {
        try (final var writer = Channels.newWriter(channel, StandardCharsets.UTF_8)) {
            WRITER.writeValue(writer, value);
        }
    }
}
