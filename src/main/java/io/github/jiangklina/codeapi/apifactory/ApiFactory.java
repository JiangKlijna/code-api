package io.github.jiangklina.codeapi.apifactory;

import io.github.jiangklina.codeapi.api.Api;

import java.io.InputStream;

/**
 * CodeApi.ApiFactory
 * 接口工厂的作用是,作用有以下两点:(不同的工厂对应的文件格式不同)
 * 1.通过文件流得到Api对象
 * 2.把Api对象转换为文件流
 *
 * @author jiangklijna
 */
public interface ApiFactory {

    /**
     * 获取该ApiFactory所能解析的文件后缀带.
     *
     * @return .ext
     */
    String getExt();

    /**
     * 1.通过文件流得到Api对象
     *
     * @param inputStream 文件流
     * @return api 接口对象
     */
    Api newApi(InputStream inputStream) throws Exception;

    /**
     * 2.把Api对象转换为文件流
     *
     * @param api 接口对象
     * @return 要转换的字节数组
     */
    byte[] toData(Api api) throws Exception;

}
