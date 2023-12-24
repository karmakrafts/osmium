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

package io.karma.ferrous.osmium;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.karma.ferrous.osmium.util.JSONUtils;
import org.apiguardian.api.API;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * @author Alexander Hinze
 * @since 21/12/2023
 */
@API(status = API.Status.STABLE)
public final class TranspilerConfig {
    @JsonProperty
    public final HashMap<String, LexerMode> modes = new HashMap<>();
    @JsonProperty
    public int version;
    @JsonProperty
    public String namespace;

    public static TranspilerConfig read(final ReadableByteChannel channel) throws IOException {
        return JSONUtils.readValue(TranspilerConfig.class, channel);
    }

    public static TranspilerConfig read(final Path path) throws IOException {
        try (final var stream = Files.newInputStream(path); final var channel = Channels.newChannel(stream)) {
            return read(channel);
        }
    }

    public void write(final WritableByteChannel channel) throws IOException {
        JSONUtils.writeValue(this, channel);
    }

    public static final class LexerMode {
        @JsonProperty
        public final HashMap<String, String> tokens = new HashMap<>();
    }
}
