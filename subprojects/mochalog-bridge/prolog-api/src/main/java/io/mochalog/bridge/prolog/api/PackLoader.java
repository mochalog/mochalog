/*
 * Copyright 2017 The Mochalog Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mochalog.bridge.prolog.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.io.IOException;

/**
 * Interface for access to Prolog packs built
 * by the io.mochalog.bridge.prolog project
 */
public class PackLoader
{
    /**
     * Retrieve a file:// resource URL to the Mochalog
     * Prolog pack distributed within the io.mochalog.bridge.prolog
     * project
     * @return Pack resource
     */
    public static URL getMochalogPackResource() throws IOException
    {
        // Retrieve file describing current Mochalog pack version
        InputStream versionStream = PackLoader.class.getResourceAsStream("/packs/mochalog-version");
        if (versionStream == null)
        {
            throw new IOException("Failed to check Mochalog pack version. Unable to fetch pack resource.");
        }

        BufferedReader versionReader = new BufferedReader(
            new InputStreamReader(versionStream, StandardCharsets.UTF_8));
        String version = versionReader.readLine();

        URL packResourceUrl = PackLoader.class.getResource("/packs/mochalog-" + version + ".zip");
        if (packResourceUrl == null)
        {
            throw new IOException("Failed to retrieve Mochalog pack resource. Resource unavailable.");
        }

        return packResourceUrl;
    }
}
