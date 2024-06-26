package com.ks.fengoj.judge.codeSanbox.remote;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;
import com.ks.fengoj.judge.codeSanbox.model.remote.Resp;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ks.fengoj.constant.CommonConstant.REMOTE_URL;

/**
 * python代码沙箱
 */
@Component
public class PythonCodeSandbox extends remoteTemplate{

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
    @Override
    public List<Resp> runFile(String fileId, List<String> inputList, String code) {
        List<Resp> respList = new ArrayList<>();
        List<String> args = Arrays.asList("E:\\Environment\\Python\\Python 3.12.1\\python", "a.python");
        List<String> env = Collections.singletonList("PATH=E:\\Environment\\Python\\Python 3.12.1;");

        for (String input : inputList) {
            JSONObject cmd = getCmd(args, env, input);

            JSONObject fileContent = new JSONObject();
            fileContent.set("content", code);
            JSONObject copyIn = new JSONObject();
            copyIn.set("a.python", fileContent);
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

}
