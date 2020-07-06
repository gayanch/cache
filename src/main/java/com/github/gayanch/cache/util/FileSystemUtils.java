package com.github.gayanch.cache.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Util class to handle file system based operations
 */
public class FileSystemUtils {
    private static final Logger log = LoggerFactory.getLogger(FileSystemUtils.class);

    private static final String STORAGE_PATH =
            Paths.get(System.getProperty("java.io.tmpdir"), "cache-storage/").toString();

    /**
     * Saves the given content in a file on STORAGE_PATH/pathPrefix/fileName
     * @param pathPrefix The path prefix to save the file
     * @param fileName The file name which the content will be saved
     * @param content The content of the file
     */
    public static void saveToFile(String pathPrefix, String fileName, Object content) {
        String filePath = Paths.get(STORAGE_PATH, pathPrefix, fileName).toString();
        createPathsIfNotExist(pathPrefix);

        log.debug("Saving {} to {} ", content, filePath);

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(content);
        } catch (IOException e) {
            log.error("Error writing file: {}", filePath, e);
        }
    }

    /**
     * Read the content of file from STORAGE_PATH/pathPrefix/fileName
     * @param pathPrefix The pathPrefix
     * @param fileName The file name to read
     * @return Content of the file. Null if not found.
     */
    public static Object readFile(String pathPrefix, String fileName) {
        String filePath = Paths.get(STORAGE_PATH, pathPrefix, fileName).toString();
        log.debug("Reading from: {}" , filePath);
        try (FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            log.error("Error reading file: {}", filePath, e);
        }

        return null;
    }

    /**
     * Removes the file in STORAGE_PATH/pathPrefix/fileName
     * @param pathPrefix The pathPrefix
     * @param fileName The name of the file to remove
     * @return Content of the file, Null if not found
     */
    public static Object removeFile(String pathPrefix, String fileName) {
        String filePath = Paths.get(STORAGE_PATH, pathPrefix, fileName).toString();

        log.debug("Removing file: {}", filePath);

        Object removedObject = readFile(pathPrefix, fileName);
        File file = new File(filePath);
        if (!file.delete()) {
            log.error("Unable to delete file at {}", filePath);
        }

        return removedObject;
    }

    /**
     * Get the file count in a given prefix location: STORAGE_PATH/pathPrefix
     * @param pathPrefix The path prefix
     * @return File count in STORAGE_PATH/pathPrefix
     */
    public static int getFileCountForPrefix(String pathPrefix) {
        String path = Paths.get(STORAGE_PATH, pathPrefix).toString();
        File directory = new File(path);
        int fileCount = 0;
        if (directory.exists()) {
            fileCount = Objects.requireNonNull(directory.list()).length;
        }
        log.debug("{} file(s) found on {} directory", fileCount, path);
        return fileCount;
    }

    /**
     * Read all files in STORAGE_PATH/pathPrefix
     * @param pathPrefix The path prefix
     * @return List of file contents
     */
    public static List<Object> readAllValuesForPrefix(String pathPrefix) {
        String path = Paths.get(STORAGE_PATH, pathPrefix).toString();
        File directory = new File(path);
        List<Object> valueList = new LinkedList<>();
        Arrays.stream(directory.list()).forEach(file -> {
            System.out.println(readFile(pathPrefix, file).toString());
        });

        return null;
    }

    /**
     * Creates a directory STORAGE_PATH/pathPrefix
     * @param pathPrefix The path prefix
     */
    private static void createPathsIfNotExist(String pathPrefix) {
        String path = Paths.get(STORAGE_PATH, pathPrefix).toString();
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
