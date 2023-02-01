package io.github.jiangklina.codeapi.filesystem.classpath;

import io.github.jiangklina.codeapi.filesystem.FileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * CodeApi.FileSystem.ClassPathFileSystem
 * 通过java Class获取文件资源
 *
 * @author jiangklijna
 */
public class ClassPathFileSystem implements FileSystem {

    private final String prefix;
    private final Class<?> mClass;

    public ClassPathFileSystem(String prefix) {
        this(prefix, ClassPathFileSystem.class);
    }

    public ClassPathFileSystem(String prefix, Class<?> mClass) {
        this.prefix = prefix;
        this.mClass = mClass;
    }

    public String getPrefix() {
        return prefix;
    }

    public Class<?> getmClass() {
        return mClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassPathFileSystem that = (ClassPathFileSystem) o;
        return Objects.equals(prefix, that.prefix) && Objects.equals(mClass, that.mClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, mClass);
    }

    @Override
    public InputStream readFile(String path) throws IOException {
        InputStream resourceAsStream = mClass.getResourceAsStream(prefix + path);
        if (resourceAsStream == null) {
            throw new FileNotFoundException(path + " is not found! from ClassPathFileSystem.");
        }
        return resourceAsStream;
    }
}
