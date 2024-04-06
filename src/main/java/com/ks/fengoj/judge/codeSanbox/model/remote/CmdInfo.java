package com.ks.fengoj.judge.codeSanbox.model.remote;

import lombok.Data;

import java.util.List;

/**
 * interface Cmd {
 * args: string[]; // command line argument
 * env?: string[]; // environment
 * <p>
 * // specifies file input / pipe collector for program file descriptors (null is reserved for pipe mapping and must be filled by in / out)
 * files?: (LocalFile | MemoryFile | PreparedFile | Collector | StreamIn | StreamOut ｜ null)[];
 * tty?: boolean; // enables tty on the input and output pipes (should have just one input & one output)
 * // Notice: must have TERM environment variables (e.g. TERM=xterm)
 * <p>
 * // limitations
 * cpuLimit?: number;     // ns
 * realCpuLimit?: number; // deprecated: use clock limit instead (still working)
 * clockLimit?: number;   // ns
 * memoryLimit?: number;  // byte
 * stackLimit?: number;   // byte (N/A on windows, macOS cannot set over 32M)
 * procLimit?: number;
 * cpuRateLimit?: number; // limit cpu usage (1000 equals 1 cpu)
 * cpuSetLimit?: string; // Linux only: set the cpuSet for cgroup
 * strictMemoryLimit?: boolean; // deprecated: use dataSegmentLimit instead (still working)
 * dataSegmentLimit?: boolean; // Linux only: use (+ rlimit_data limit) enable by default if cgroup not enabled
 * addressSpaceLimit?: boolean; // Linux only: use (+ rlimit_address_space limit)
 * <p>
 * // copy the correspond file to the container dst path
 * copyIn?: {[dst:string]:LocalFile | MemoryFile | PreparedFile | Symlink};
 * <p>
 * // copy out specifies files need to be copied out from the container after execution
 * // append '?' after file name will make the file optional and do not cause FileError when missing
 * copyOut?: string[];
 * // similar to copyOut but stores file in go judge and returns fileId, later download through /file/:fileId
 * copyOutCached?: string[];
 * // specifies the directory to dump container /w content
 * copyOutDir: string
 * // specifies the max file size to copy out
 * copyOutMax?: number; // byte
 * }
 */
@Data
public class CmdInfo {
    private List<String> args;

    private List<String> env;

    private List<FileInfo> files;

    private Long cpuLimit;

    private Long realCpuLimit;

    private Long clockLimit;

    private Long memoryLimit;

    private Long stackLimit;

    private Long procLimit;

    private Long cpuRateLimit;

    private String copyIn;

    private List<String> copyOut;

    private List<String> copyOutCached;

    private String copyOutDir;

    private Long copyOutMax;
}
