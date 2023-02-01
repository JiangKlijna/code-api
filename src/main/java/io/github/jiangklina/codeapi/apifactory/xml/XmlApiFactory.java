package io.github.jiangklina.codeapi.apifactory.xml;

import cn.hutool.core.util.XmlUtil;
import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.apifactory.ApiFactory;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * CodeApi.ApiFactory.YmlApiFactory
 * 转换xml格式的api
 *
 * @author jiangklijna
 */
public class XmlApiFactory implements ApiFactory {

    @Override
    public String getExt() {
        return ".xml";
    }

    /**
     * 将xml格式的文件流转换为Api对象
     * 使用Dom4j
     *
     * @param inputStream 文件流
     * @return Api对象
     * @throws Exception 转换时的异常
     */
    @Override
    public Api newApi(InputStream inputStream) throws Exception {
        org.w3c.dom.Document document = XmlUtil.readXML(inputStream);
        inputStream.close();
        return XmlUtil.xmlToBean(document, Api.class);
    }

    /**
     * 将Api接口对象按照xml格式转换为字节数组
     * 使用Dom4j
     *
     * @param api 对象
     * @return 字节数组
     * @throws Exception 转换时的异常
     */
    @Override
    public byte[] toData(Api api) throws Exception {
        Document document = XmlUtil.beanToXml(api);
        return XmlUtil.toStr(document).getBytes(StandardCharsets.UTF_8);
    }
}
