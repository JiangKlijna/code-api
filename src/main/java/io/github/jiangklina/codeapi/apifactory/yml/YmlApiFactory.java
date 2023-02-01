package io.github.jiangklina.codeapi.apifactory.yml;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.apifactory.ApiFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * CodeApi.ApiFactory.YmlApiFactory
 * 转换yml格式的api
 *
 * @author jiangklijna
 */
public class YmlApiFactory implements ApiFactory {

    @Override
    public String getExt() {
        return ".yml";
    }

    /**
     * 将yml格式的文件流转换为Api对象
     * 使用Hutool.YamlUtil
     *
     * @param inputStream 文件流
     * @return Api对象
     * @throws Exception 转换时的异常
     */
    @Override
    public Api newApi(InputStream inputStream) throws Exception {
        Dict load = YamlUtil.load(new InputStreamReader(inputStream));
        inputStream.close();
        return load.toBean(Api.class);
//        String commonCode = load.getStr("common");
//        Map<String, String> otherCode = load.get("other", Collections.emptyMap());
//        List<Map<String, Object>> paramsYml = load.get("params", Collections.emptyList());
//        List<Param> params = paramsYml.stream().map(e -> {
//            String key = (String) e.getOrDefault("key", "");
//            String defaultValue = (String) e.getOrDefault("default", "");
//            String type = (String) e.getOrDefault("type", "");
//            String require = (String) e.getOrDefault("require", "");
//            String format = (String) e.getOrDefault("format", "");
//            if (StringUtils.isBlank(require)) {
//                require = "false";
//            }
//            return new Param(key, defaultValue, type, "true".equalsIgnoreCase(require), format);
//        }).collect(Collectors.toList());
//
//        inputStream.close();
//        return new Api(commonCode, params, otherCode);
    }

    /**
     * 将Api接口对象按照yml格式转换为字节数组
     * 使用Dom4j
     *
     * @param api 对象
     * @return 字节数组
     * @throws Exception 转换时的异常
     */
    @Override
    public byte[] toData(Api api) throws Exception {
//        DumperOptions dumperOptions = new DumperOptions();
//        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.LITERAL);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        YamlUtil.dump(api, new OutputStreamWriter(byteArray));
        return byteArray.toByteArray();
    }
}
