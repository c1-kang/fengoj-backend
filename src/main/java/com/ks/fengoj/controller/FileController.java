package com.ks.fengoj.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ks.fengoj.common.BaseResponse;
import com.ks.fengoj.constant.FileConstant;
import com.ks.fengoj.manager.CosManager;
import com.ks.fengoj.model.dto.file.UploadFileRequest;
import com.ks.fengoj.model.enums.CompressFileSuffixEnum;
import com.ks.fengoj.service.UserService;
import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.common.ResultUtils;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.model.entity.User;
import com.ks.fengoj.model.enums.FileUploadBizEnum;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 解压缩特定后缀文件
     *
     * @param zipFilePath     文件路径
     * @param outputDirectory 输出路径
     * @return 输入输出样例是否相等
     * @throws IOException IOException
     */
    private static Boolean unzipSpecificExtension(String zipFilePath, String outputDirectory) throws IOException {
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

    /**
     * 提取文件
     *
     * @param zipFile        zip 文件
     * @param entry          entry
     * @param outputFilePath 输出路径
     * @throws IOException IOException
     */
    private static void extractEntry(ZipFile zipFile, ZipArchiveEntry entry, Path outputFilePath) throws IOException {
        try (InputStream inputStream = zipFile.getInputStream(entry);
             FileOutputStream outputStream = new FileOutputStream(outputFilePath.toFile())) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    /**
     * 递归删除文件夹
     *
     * @param folder 文件夹
     */
    public static void deleteFolder(File folder) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles();
        if (files != null) { // 确保文件夹不为空
            for (File file : files) {
                // 如果是文件，直接删除
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) { // 如果是子文件夹，递归删除
                    deleteFolder(file);
                }
            }
        }
        // 删除空文件夹
        folder.delete();
    }

    /**
     * 文件上传
     *
     * @param multipartFile     multipartFile
     * @param uploadFileRequest uploadFileRequest
     * @param request           request
     * @return 可访问地址
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);

        // 文件目录：根据业务、用户来划分
        User loginUser = userService.getLoginUser(request);
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        if (fileUploadBizEnum.equals(FileUploadBizEnum.USER_AVATAR)) {
            String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
            File file = null;
            try {
                // 上传文件
                file = File.createTempFile(filepath, null);
                multipartFile.transferTo(file);
                cosManager.putObject(filepath, file);
                // 返回可访问地址
                return ResultUtils.success(FileConstant.COS_HOST + filepath);
            } catch (Exception e) {
                log.error("file upload error, filepath = " + filepath, e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
            } finally {
                if (file != null) {
                    // 删除临时文件
                    boolean delete = file.delete();
                    if (!delete) {
                        log.error("file delete error, filepath = {}", filepath);
                    }
                }
            }
        }

        // 保存判题样例
        if (fileUploadBizEnum.equals(FileUploadBizEnum.JUDGE_CASE)) {
            String mainPath = System.getProperty("user.dir") + File.separator + "JudgeCaseDir";
            File file = new File(mainPath);
            if (!file.exists()) {
                file.mkdir();
            }
            String loginUserPath = mainPath + File.separator + loginUser.getId();
            file = new File(loginUserPath);
            if (!file.exists()) {
                file.mkdir();
            }
            String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
            String filepath = String.format(loginUserPath + File.separator + uuid);
            file = new File(filepath);
            if (!file.exists()) {
                file.mkdir();
            }
            File file1 = null;
            try {
                file1 = File.createTempFile(filepath + File.separator + "temp", null);
                multipartFile.transferTo(file1);
                if (CompressFileSuffixEnum.ZIP.getValue().equals(fileSuffix)) {
                    Boolean res = unzipSpecificExtension(file1.getAbsolutePath(), filepath);
                    if (!res) {
                        deleteFolder(file);
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入输出样例不等");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                file1.deleteOnExit();
            }
            return ResultUtils.success(filepath);
        }
        return null;
    }

    /**
     * 校验文件
     *
     * @param multipartFile     multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        } else if (FileUploadBizEnum.JUDGE_CASE.equals(fileUploadBizEnum)) {
            if (fileSize > 20 * ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 20M");
            }
            if (!Arrays.asList("zip", "7z", "rar").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}