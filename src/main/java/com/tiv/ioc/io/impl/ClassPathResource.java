package com.tiv.ioc.io.impl;

import com.tiv.ioc.exception.BeanException;
import com.tiv.ioc.io.Resource;

import java.io.InputStream;

/**
 * 类路径资源类
 */
public class ClassPathResource implements Resource {

    private String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        this.classLoader = classLoader;
    }

    @Override
    public InputStream getInputStream() {
        InputStream inputStream;
        if (this.classLoader == null) {
            inputStream = ClassLoader.getSystemResourceAsStream(path);
        } else {
            inputStream = classLoader.getResourceAsStream(path);
        }

        if (inputStream == null) {
            throw new BeanException("file is not exist");
        }
        return inputStream;
    }
}
