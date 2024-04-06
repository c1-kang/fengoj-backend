package com.ks.fengoj.judge.codeSanbox.remote;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.judge.codeSanbox.model.remote.Resp;
import com.ks.fengoj.judge.codeSanbox.model.remote.RespFile;
import com.ks.fengoj.model.enums.JudgeInfoMessageEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ks.fengoj.constant.CommonConstant.REMOTE_URL;

/**
 * 远程调用模板-初始 java
 */
public abstract class remoteTemplate {

    private static final List<String> NO_COMPILE = Arrays.asList("python", "javascript");

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        // 语言是否需要编译
        boolean flag = NO_COMPILE.contains(language);

        String fileId = null;
        if (!flag) {
            // 1、将请求处理成符合调用代码沙箱的请求
            String request = getRequest(code);

            // 2、请求接口进行编译，获得文件ID
            fileId = compileFile(request);
        }

        // 3、运行文件，得到结果
        String tmpcode = "";
        if (flag) tmpcode = code;
        List<Resp> respList = runFile(fileId, inputList, tmpcode);

        // 4、处理结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        List<String> outputList = new ArrayList<>();
        List<JudgeInfo> judgeInfoList = new ArrayList<>();

        for (Resp resp : respList) {
            if (resp.getStatus().equals("Accepted")) {
                Long time = resp.getTime();
                Long memory = resp.getMemory();
                RespFile files = resp.getFiles();
                String stdout = files.getStdout();
                outputList.add(stdout);

                JudgeInfo judgeInfo = new JudgeInfo();
                judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
                judgeInfo.setMemory(memory / 1000000);
                judgeInfo.setTime(time / (1000 * 1000));
                judgeInfoList.add(judgeInfo);
            } else {
                String status = resp.getStatus();
                RespFile files = resp.getFiles();
                String stderr = files.getStderr();

            }
        }
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setOutputList(outputList);
        executeCodeResponse.setJudgeInfo(judgeInfoList);

        // 5、删除文件
        if (!flag) {
            HttpRequest.delete(REMOTE_URL + "/file/" + fileId).execute().body();
        }

        return executeCodeResponse;
    }

    /**
     * 构建请求
     *
     * @param code 代码
     * @return request
     */
    public String getRequest(String code) {
        List<String> env = Collections.singletonList("PATH=E:\\Environment\\Java\\jdk-17\\bin");
        List<String> args = Arrays.asList("E:\\Environment\\Java\\jdk-17\\bin\\javac", "Main.java");

        JSONObject cmd = getCmd(args, env, "");

        JSONObject fileContent = new JSONObject();
        fileContent.set("content", code);
        JSONObject copyIn = new JSONObject();
        copyIn.set("Main.java", fileContent);
        cmd.set("copyIn", copyIn);
        cmd.set("copyOutCached", new JSONArray().put("Main.class"));

        JSONObject param = new JSONObject();
        param.set("cmd", new JSONArray().put(cmd));
        return param.toString();
    }

    /**
     * 编译文件，获取缓存编译文件ID
     *
     * @param json requestJson
     * @return ID
     */
    public String compileFile(String json) {
        String body = HttpRequest.post(REMOTE_URL + "/run")
                .body(json)
                .execute().body();

        JSONObject res = (JSONObject) JSONUtil.parseArray(body).get(0);
        JSONObject o = (JSONObject) res.get("fileIds");
        return (String) o.get("Main.class");
    }

    public List<Resp> runFile(String fileId, List<String> inputList, String code) {
        List<Resp> respList = new ArrayList<>();
        List<String> args = Arrays.asList("E:\\Environment\\Java\\jdk-17\\bin\\java", "Main");
        List<String> env = Collections.singletonList("PATH=E:\\Environment\\Java\\jdk-17\\bin");

        for (String input : inputList) {
            JSONObject cmd = getCmd(args, env, input);

            JSONObject fileContent = new JSONObject();
            fileContent.set("fileId", fileId);
            JSONObject copyIn = new JSONObject();
            copyIn.set("Main.class", fileContent);
            cmd.set("copyIn", copyIn);

            JSONObject param = new JSONObject();
            param.set("cmd", new JSONArray().put(cmd));
            String json = param.toString();

            String body = HttpRequest.post(REMOTE_URL + "/run")
                    .body(json)
                    .execute().body();

            JSONObject res = (JSONObject) JSONUtil.parseArray(body).get(0);
            Resp resp = JSONUtil.toBean(res, Resp.class);
            respList.add(resp);
        }

        return respList;
    }

    public JSONObject getCmd(List<String> args, List<String> env, String input) {
        JSONObject cmd = new JSONObject();

        cmd.set("args", args);
        cmd.set("env", env);

        // files
        JSONArray files = new JSONArray();
        JSONObject content = new JSONObject();
        content.set("content", input);

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * 32);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * 32);
        files.put(content);
        files.put(stdout);
        files.put(stderr);
        cmd.set("files", files);

        // ms-->ns
        cmd.set("cpuLimit", 10000000000L);

        // byte
        cmd.set("memoryLimit", 512 * 1000 * 1000L);
        cmd.set("procLimit", 50);

        return cmd;
    }
}
