package com.tiv.ioc.io;

/**
 * 资源加载器接口
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 根据资源路径获取资源
     *
     * @param location
     * @return
     */
    Resource getResource(String location);

    /**
     * 获取类加载器
     *
     * @return
     */
    ClassLoader getClassLoader();
}
