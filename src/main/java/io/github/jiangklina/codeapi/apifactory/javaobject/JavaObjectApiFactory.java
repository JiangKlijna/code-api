package io.github.jiangklina.codeapi.apifactory.javaobject;

import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.apifactory.ApiFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * CodeApi.ApiFactory.JavaObjectApiFactory
 * 转换javaobject格式的api,主要是用来测试fa架构的正确性,实际不用他
 *
 * @author jiangklijna
 */
public class JavaObjectApiFactory implements ApiFactory {

    @Override
    public String getExt() {
        return ".javaobject";
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
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Api api = (Api) objectInputStream.readObject();
        inputStream.close();
        return api;
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
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(api);
        return byteArrayOutputStream.toByteArray();
    }
}
