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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URISyntaxException;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Interface for access to Prolog packs built
 * by the io.mochalog.bridge.prolog project
 */
public class PackLoader
{
    /**
     * Get the file path for a Prolog pack from io.mochalog.bridge.prolog
     * with the specified name.
     * @param name Pack name
     * @return Path string
     * @throws IOException IO error occurred.
     */
    public static String getPackResource(String name) throws IOException
    {
        // Get the most recent pack version
        String version = getPackVersion(name);
        // Determine the corresponding archive name
        // according to naming conventions
        String packArchiveFile = name + "-" + version + ".zip";

        Path rootDir;
        try
        {
            URL enclosingArchiveUrl = PackLoader.class.getProtectionDomain().getCodeSource().getLocation();
            Path enclosingArchivePath = Paths.get(enclosingArchiveUrl.toURI());

            // Get directory containing outer pack archive
            rootDir = enclosingArchivePath.getParent();
        }
        catch (URISyntaxException e)
        {
            throw new IOException("Failed to access pack extraction directory.");
        }

        // Create directory for storing extracted pack archives (if not
        // already created)
        Path packArchiveExtractDir = rootDir.resolve("extracted-packs");
        if (!Files.exists(packArchiveExtractDir))
        {
            Files.createDirectory(packArchiveExtractDir);
        }

        // Copy the specified pack into the extraction directory
        // given it has not already been extracted
        Path packArchiveExtractFilePath = packArchiveExtractDir.resolve(packArchiveFile);
        if (!Files.exists(packArchiveExtractFilePath))
        {
            InputStream packResourceStream =
                    PackLoader.class.getResourceAsStream("/packs/" + packArchiveFile);
            if (packResourceStream == null)
            {
                throw new IOException("Failed to retrieve pack resource. Resource " + name + " unavailable.");
            }

            Files.copy(packResourceStream, packArchiveExtractFilePath);
        }

        // Ensure pack path is provided in platform-agnostic file syntax
        return packArchiveExtractFilePath.toString().replace('\\', '/');
    }

    /**
     * Get the most recent version of the Prolog pack with the
     * given name.
     * @param name Pack name
     * @return Pack version string
     * @throws IOException IO error occurred.
     */
    public static String getPackVersion(String name) throws IOException
    {
        // Retrieve file describing current pack version
        InputStream versionStream = PackLoader.class.getResourceAsStream("/packs/" + name + "-version");
        if (versionStream == null)
        {
            throw new IOException("Failed to check pack version. Unable to fetch pack resource " + name + ".");
        }

        BufferedReader versionReader = new BufferedReader(
                new InputStreamReader(versionStream, StandardCharsets.UTF_8));
        return versionReader.readLine();
    }
}
