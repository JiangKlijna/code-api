package io.github.jiangklina.codeapi.apifactory.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.apifactory.ApiFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * CodeApi.ApiFactory.YmlApiFactory
 * 转换json格式的api
 *
 * @author jiangklijna
 */
public class JsonApiFactory implements ApiFactory {

    @Override
    public String getExt() {
        return ".json";
    }

    /**
     * 将json格式的文件流转换为Api对象
     * 使用FastJson
     *
     * @param inputStream 文件流
     * @return Api对象
     * @throws Exception 转换时的异常
     */
    @Override
    public Api newApi(InputStream inputStream) throws Exception {
        JSONObject jsonObject = JSONUtil.parseObj(inputStream);
        inputStream.close();
        return jsonObject.toBean(Api.class);
    }

    /**
     * 将Api接口对象按照json格式转换为字节数组
     * 使用Dom4j
     *
     * @param api 对象
     * @return 字节数组
     * @throws Exception 转换时的异常
     */
    @Override
    public byte[] toData(Api api) throws Exception {
        return JSONUtil.toJsonStr(api).getBytes(StandardCharsets.UTF_8);
    }
}
