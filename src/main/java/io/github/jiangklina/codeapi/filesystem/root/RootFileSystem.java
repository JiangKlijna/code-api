package io.github.jiangklina.codeapi.filesystem.root;

import io.github.jiangklina.codeapi.filesystem.FileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * CodeApi.FileSystem.ClassPathFileSystem
 * 通过真实的文件系统获取文件
 *
 * @author jiangklijna
 */
public class RootFileSystem implements FileSystem {

    private final String root;

    public RootFileSystem(String root) {
        this.root = new File(root).getAbsolutePath();
    }

    public String getRoot() {
        return root;
    }

    @Override
    public InputStream readFile(String path) throws IOException {
        File file = new File(root, path);
        return new FileInputStream(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootFileSystem that = (RootFileSystem) o;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

}
