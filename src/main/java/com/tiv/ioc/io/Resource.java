package com.tiv.ioc.io;

import java.io.InputStream;

/**
 * 资源接口
 */
public interface Resource {

    /**
     * 获取资源输入流
     *
     * @return
     */
    InputStream getInputStream();
}
