package com.ks.fengoj.judge.codeSanbox.model.remote;

import lombok.Data;

/**
 * interface LocalFile {
 * src: string; // absolute path for the file
 * }
 * <p>
 * interface MemoryFile {
 * content: string | Buffer; // file contents
 * }
 * <p>
 * interface PreparedFile {
 * fileId: string; // fileId defines file uploaded by /file
 * }
 */
@Data
public class FileInfo {

    private String name;

    private String src;

    private String content;

    private String fileId;

    private Long max; // 10240
}
