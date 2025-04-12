package com.tiv.ioc.io.impl;

import com.tiv.ioc.io.Resource;
import com.tiv.ioc.io.ResourceLoader;

import java.net.URL;

/**
 * 默认资源加载器类
 */
public class DefaultResourceLoader implements ResourceLoader {

    private ClassLoader classLoader;

    public DefaultResourceLoader() {
        try {
            this.classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception ignore) {

        }

        if (this.classLoader == null) {
            this.classLoader = DefaultResourceLoader.class.getClassLoader();
        }
    }

    @Override
    public Resource getResource(String location) {
        if (location.startsWith("/")) {
            return new ClassPathContextResource(location, getClassLoader());
        }
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        }

        try {
            URL url = new URL(location);
            return isFieldUrl(url) ? new FileUrlResource(url) : new UrlResource(url);
        } catch (Exception e) {
            return new ClassPathContextResource(location, getClassLoader());
        }
    }

    public boolean isFieldUrl(URL url) {
        String protocol = url.getProtocol();
        return "file".equals(protocol)
                || "vfs".equals(protocol)
                || "vfsfile".equals(protocol);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    protected static class ClassPathContextResource extends ClassPathResource {

        public ClassPathContextResource(String path, ClassLoader classLoader) {
            super(path, classLoader);
        }
    }
}
