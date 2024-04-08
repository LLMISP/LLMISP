package edu.univ.lab.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileUtils {

    /**
     * LineBreak
     * <p>
     */
    enum LineBreak {
        /**
         * LF -> \n
         */
        LF("\n"),

        /**
         * CRLF -> \r\n
         */
        CRLF("\r\n");

        private final String content;

        LineBreak(String lineBreak) {
            this.content = lineBreak;
        }

        @Override
        public String toString() {
            return this.content;
        }
    }

    public static String readFile(String url) {
        FileInputStream fis = null;
        StringBuilder line = new StringBuilder();
        try {
            fis = new FileInputStream(url);
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(data)) != -1) {
                line.append(new String(data, 0, bytesRead));
            }
        } catch (Exception e) {
            System.out.println("Cannot read file: " + url);
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line.toString();
    }

    public static void writeFile(String url, String content, boolean append) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(url, append);
            byte[] data = content.getBytes(StandardCharsets.UTF_8);
            fos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getDirPath
     * <p>
     *
     * @return
     */
    public static Optional<String> getDirPath(String filePath) {
        String parentPath = new File(filePath).getParent();
        if (parentPath != null) {
            return Optional.of(parentPath);
        }
        return Optional.empty();
    }

    /**
     * getFileName
     * <p>
     *
     * @return
     */
    public static Optional<String> getFileName(String filePath) {
        if (filePath.contains(File.separator)) {
            return Optional.of(new File(filePath).getName());
        }
        return Optional.empty();
    }

    /**
     * getFileAfterCreateFolder
     * <p>
     * @return
     */
    public static Optional<File> getFileAfterCreateFolder(String filePath) throws IOException {
        Optional<String> dirOpt = FileUtils.getDirPath(filePath);
        if (dirOpt.isEmpty()) {
            return Optional.empty();
        }
        File dir = new File(dirOpt.get());
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException(
                    "getFileAfterCreateFolder cannot create a dir: " + dirOpt.get());
        }
        return Optional.of(new File(filePath));
    }

    public static void integrateFolders(String destPath, List<String> paths) {
        for (String path : paths) {
            handleDir(path, destPath);
        }
    }

    private static void handleDir(String path, String destPath) {
        if (!new File(destPath).exists() && !new File(destPath).mkdirs()) {
            System.out.println(destPath + " doesn't exist but cannot be created");
            return;
        }
        File src = new File(path);
        if (src.isFile()) return;
        File[] innerFile = src.listFiles();
        if (innerFile == null) return;

        for (File file : innerFile) {
            String nextSrcPath = path + File.separator + file.getName();
            String nextDestPath = destPath + File.separator + file.getName();
            if (file.isDirectory()) {
                if (new File(nextDestPath).exists()) {
                    handleDir(nextSrcPath, nextDestPath);
                } else {
                    new File(nextSrcPath).renameTo(new File(nextDestPath));
                }
            } else {
                if (new File(nextDestPath).exists()) {
                    System.out.println(nextSrcPath + " has existed in " + nextDestPath);
                } else {
                    new File(nextSrcPath).renameTo(new File(nextDestPath));
                }
            }
        }
    }
}