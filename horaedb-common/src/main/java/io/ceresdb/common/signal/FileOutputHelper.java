/*
 * Copyright 2023 CeresDB Project Authors. Licensed under Apache-2.0.
 */
package io.ceresdb.common.signal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.ceresdb.common.OptKeys;
import io.ceresdb.common.util.Files;
import io.ceresdb.common.util.SystemPropertyUtil;

public final class FileOutputHelper {

    private static final String OUT_DIR = SystemPropertyUtil.get(OptKeys.SIG_OUT_DIR, "");

    public static File getOutputFile(final String baseFileName) throws IOException {
        Files.mkdirIfNotExists(OUT_DIR);
        final String now = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        final String fileName = baseFileName + "." + now;
        final File file = Paths.get(OUT_DIR, fileName).toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Fail to create file: " + file);
        }
        return file;
    }

    public static String getOutDir() {
        return Paths.get(OUT_DIR).toAbsolutePath().toString();
    }

    private FileOutputHelper() {
    }
}
