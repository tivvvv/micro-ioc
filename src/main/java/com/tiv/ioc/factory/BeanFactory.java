package com.tiv.ioc.factory;

/**
 * bean工厂接口
 */
public interface BeanFactory {

    /**
     * 根据bean名称获取bean对象
     *
     * @param name
     * @return
     */
    Object getBean(String name);

    /**
     * 根据bean类型获取bean对象
     *
     * @param requiredType
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> requiredType);

    /**
     * 根据bean名称和类型获取bean对象
     *
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     */
    <T> T getBean(String name, Class<T> requiredType);
}
