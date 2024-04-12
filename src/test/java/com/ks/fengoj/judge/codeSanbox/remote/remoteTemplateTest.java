package com.ks.fengoj.judge.codeSanbox.remote;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class remoteTemplateTest {

    private static final List<String> ENV = Collections.singletonList("PATH=E:\\Environment\\Java\\jdk-17\\bin");

    private static final List<String> ARGS = Arrays.asList("E:\\Environment\\Java\\jdk-17\\bin\\javac", "Main.java");

    @Test
    public void invoke() {
        String code = "import java.util.Scanner; class Main {public static void main(String[] args) {Scanner scan = new Scanner(System.in);int a = scan.nextInt();int b = scan.nextInt();System.out.println(a+b);}}";
        String request = getRequest(code);

        String body = HttpRequest.post("http://localhost:5050/run")
                .body(request)
                .execute().body();

        JSONObject res = (JSONObject) JSONUtil.parseArray(body).get(0);
        JSONObject o = (JSONObject) res.get("fileIds");
        String id = (String)o.get("Main.class");
        System.out.println(id);
    }

    @Test
    public void delete() {
        String fileId = "JQFITEEY";
        String result = HttpRequest.delete("http://127.0.0.1:5050/file/" + fileId).execute().body();
    }

    /**
     * 构建请求
     *
     * @param code 代码
     * @return json
     */
    private String getRequest(String code) {
        JSONObject cmd = new JSONObject();
        cmd.set("args", ARGS);
        cmd.set("env", ENV);

        // files
        JSONArray COMPILE_FILES = new JSONArray();
        JSONObject content = new JSONObject();
        content.set("content", "");

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * 32);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * 32);
        COMPILE_FILES.put(content);
        COMPILE_FILES.put(stdout);
        COMPILE_FILES.put(stderr);
        cmd.set("files", COMPILE_FILES);

        // ms-->ns
        cmd.set("cpuLimit", 10000000000L);

        // byte
        cmd.set("memoryLimit", 512 * 1000 * 1000L);
        cmd.set("procLimit", 50);

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
}