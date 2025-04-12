package com.tiv.ioc.io.impl;

import com.tiv.ioc.io.Resource;

import java.io.InputStream;
import java.net.URL;

/**
 * Url资源类
 */
public class UrlResource implements Resource {

    private URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() {
        return null;
    }
}
