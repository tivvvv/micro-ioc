package com.tiv.ioc.bean;

/**
 * bean定义信息接口
 */
public interface BeanDefinition {

    /**
     * 获取bean名称
     *
     * @return
     */
    String getBeanName();

    /**
     * 设置bean名称
     *
     * @param beanName
     */
    void setBeanName(String beanName);

    /**
     * 获取bean类名
     *
     * @return
     */
    String getBeanClassName();

    /**
     * 设置bean类名
     *
     * @param beanClassName
     */
    void setBeanClassName(String beanClassName);
}
