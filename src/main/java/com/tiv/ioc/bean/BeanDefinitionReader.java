package com.tiv.ioc.bean;

import com.tiv.ioc.io.Resource;
import com.tiv.ioc.io.ResourceLoader;

import java.util.List;

/**
 * bean定义信息读取器
 */
public interface BeanDefinitionReader {

    /**
     * 获取资源加载器
     *
     * @return
     */
    ResourceLoader getResourceLoader();

    /**
     * 根据资源定位,加载bean定义信息
     *
     * @param location
     * @return
     */
    List<BeanDefinition> loadBeanDefinitions(String location);

    /**
     * 根据资源定位,批量加载bean定义信息
     *
     * @param locations
     * @return
     */
    List<BeanDefinition> loadBeanDefinitions(String... locations);

    /**
     * 根据资源,加载bean定义信息
     *
     * @param resource
     * @return
     */
    List<BeanDefinition> loadBeanDefinitions(Resource resource);

    /**
     * 根据资源,批量加载bean定义信息
     *
     * @param resources
     * @return
     */
    List<BeanDefinition> loadBeanDefinitions(Resource... resources);
}
