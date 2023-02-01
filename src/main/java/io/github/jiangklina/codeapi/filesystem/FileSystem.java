package io.github.jiangklina.codeapi.filesystem;

import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.apifactory.ApiFactory;
import io.github.jiangklina.codeapi.apifactory.json.JsonApiFactory;
import io.github.jiangklina.codeapi.apifactory.xml.XmlApiFactory;
import io.github.jiangklina.codeapi.apifactory.yml.YmlApiFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * CodeApi.FileSystem
 * 文件系统的作用是,通过[请求路径]获取到Api,以及优先获取Xml格式的Api,其次是Json,yml格式的等等
 *
 * @author jiangklijna
 */
public interface FileSystem {

    /**
     * 在这里注册所有的apiFactory类型
     * 依次查找对应api文件,找到则创建Api
     */
    List<ApiFactory> apiFactories = Collections.unmodifiableList(Arrays.asList(
            new XmlApiFactory(),
            new JsonApiFactory(),
            new YmlApiFactory()
    ));

    /**
     * 根据路径获取文件流,不存在则抛异常
     *
     * @param path 路径
     * @return InputStream永远不会为null
     * @throws IOException 未读取到文件
     */
    InputStream readFile(String path) throws IOException;

    /**
     * 通过path读取对应Api
     *
     * @param path 路径
     * @return Api
     */
    default Api readApi(String path) throws IOException {
        return readApi(path, "");
    }

    /**
     * 根据apiType读取api,如果没传apiType那就轮询查找
     *
     * @param path    api路径
     * @param apiType api类型后缀名带.
     * @return Api
     */
    default Api readApi(String path, String apiType) throws IOException {
        InputStream inputStream = null;
        IOException ioException = null;
        boolean isEmptyApiType = apiType == null || apiType.isEmpty();
        for (ApiFactory apiFactory : apiFactories) {
            String ext = apiFactory.getExt();
            if (!isEmptyApiType && !ext.equals(apiType)) { // 传了apiType就必须用指定的apiFactory
                continue;
            }
            try {
                inputStream = readFile(path + ext);
            } catch (IOException e) {
                ioException = new IOException(path + " is not found!", e);
            }
            if (inputStream != null) try {
                return apiFactory.newApi(inputStream);
            } catch (Exception e) {
                ioException = new IOException("failed to parse file [" + (path + ext) + "], " + e.getMessage(), e);
            }
        }
        if (ioException == null) {
            ioException = new FileNotFoundException(path + apiType + " is not found!");
        }
        throw ioException;
    }

}
