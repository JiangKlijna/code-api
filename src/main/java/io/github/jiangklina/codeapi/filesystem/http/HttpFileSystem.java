package io.github.jiangklina.codeapi.filesystem.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import io.github.jiangklina.codeapi.filesystem.FileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * CodeApi.FileSystem.HttpFileSystem
 * 通过http server获取文件流
 *
 * @author jiangklijna
 */
public class HttpFileSystem implements FileSystem {

    private final String prefix;

    public HttpFileSystem(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public InputStream readFile(String path) throws IOException {
        HttpRequest get = HttpUtil.createGet(prefix + path);
        HttpResponse execute = get.execute();
        return execute.bodyStream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpFileSystem that = (HttpFileSystem) o;
        return Objects.equals(prefix, that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix);
    }

}
