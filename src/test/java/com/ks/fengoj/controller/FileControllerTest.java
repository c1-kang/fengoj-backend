package com.ks.fengoj.controller;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SpringBootTest
class FileControllerTest {

    /**
     * 读取文件夹里的所有文件，把文件合成 String
     * @throws IOException IOException
     */
    @Test
    void TestString() throws IOException {
        String filePath = "E:\\Code\\Project\\OJ\\fengoj-backend\\JudgeCaseDir\\1700433410457255938\\QauTqwol\\in";
        File file = new File(filePath);
        List<String> list = new ArrayList<>();
        for (File listFile : file.listFiles()) {
            String absolutePath = listFile.getAbsolutePath();
            String s = new String(Files.readAllBytes(Paths.get(absolutePath)));
//            System.out.println(s);
            list.add(s);
        }
        System.out.println(list.get(0));
    }

    @Test
    void TestCompress() throws IOException {
        // 指定zip文件路径
        String zipFilePath = "E:\\Code\\c++\\leetcode\\leetcode.zip";
        // 指定解压后文件存放的目录
        String outputDirectory = "E:\\Code\\c++\\leetcode\\";

        // 创建临时目录用于存放解压后的文件
        File tempDir = new File(outputDirectory + File.separator + "output");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        // 使用Apache Commons Compress库解压zip文件
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                InputStream inputStream = zipFile.getInputStream(entry);
                File outputFile = new File(tempDir, entry.getName());
                try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                    IOUtils.copy(inputStream, outputStream);
                }
            }
        }

        // 按照后缀名分类并保存到List<File>中
        Map<String, List<File>> filesByExtension = new HashMap<>();
        for (File file : Objects.requireNonNull(tempDir.listFiles())) {
            String extension = getFileExtension(file);
            filesByExtension.computeIfAbsent(extension, k -> new ArrayList<>()).add(file);
        }

        // 打印结果
        for (Map.Entry<String, List<File>> entry : filesByExtension.entrySet()) {
            System.out.println("Extension: " + entry.getKey());
            for (File file : entry.getValue()) {
                System.out.println("  File: " + file.getAbsolutePath());
            }
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        } else {
            return "unknown";
        }
    }

    /**
     * 解压zip文件
     * @throws IOException IOException
     */
    @Test
    public void Test2() throws IOException {
        String zipFilePath = "E:\\Code\\c++\\leetcode\\leetcode.zip";
        String outputDirectory = "E:\\Code\\c++\\leetcode\\";

        Boolean b = unzipSpecificExtension(zipFilePath, outputDirectory);
        System.out.println(b);
    }

    /**
     * 根据特定后缀过滤
     * @param zipFilePath zipFilePath
     * @param outputDirectory outputDirectory
     * @throws IOException IOException
     */
    public static Boolean unzipSpecificExtension(String zipFilePath, String outputDirectory) throws IOException {
        Path outputPath = Paths.get(outputDirectory);
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
        }
        Path outputPath_out = outputPath.resolve("out" + File.separator);
        if (!Files.exists(outputPath_out)) {
            Files.createDirectories(outputPath_out);
        }
        Path outputPath_in = outputPath.resolve("in" + File.separator);
        if (!Files.exists(outputPath_in)) {
            Files.createDirectories(outputPath_in);
        }

        HashSet<String> inSet = new HashSet<>();
        HashSet<String> outSet = new HashSet<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (entry.getName().endsWith("in")) {
                    inSet.add(StrUtil.removeSuffix(entry.getName(), ".in"));
                    Path outputFilePath = outputPath_in.resolve(entry.getName());
                    extractEntry(zipFile, entry, outputFilePath);
                } else if (entry.getName().endsWith("out")) {
                    outSet.add(StrUtil.removeSuffix(entry.getName(), ".out"));
                    Path outputFilePath = outputPath_out.resolve(entry.getName());
                    extractEntry(zipFile, entry, outputFilePath);
                }
            }
        }
        if (inSet.isEmpty() || inSet.size() != outSet.size()) {
            return false;
        }
        for (String s : inSet) {
            if (!outSet.contains(s)) {
                return false;
            }
        }
        return true;
    }

    private static void extractEntry(ZipFile zipFile, ZipArchiveEntry entry, Path outputFilePath) throws IOException {
        try (InputStream inputStream = zipFile.getInputStream(entry);
             FileOutputStream outputStream = new FileOutputStream(outputFilePath.toFile())) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}