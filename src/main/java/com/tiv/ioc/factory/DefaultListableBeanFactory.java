package com.tiv.ioc.factory;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.tiv.ioc.bean.BeanDefinition;
import com.tiv.ioc.bean.GenericBeanDefinition;
import com.tiv.ioc.exception.BeanException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean工厂默认实现类
 */
public class DefaultListableBeanFactory implements BeanFactory {

    /**
     * bean定义文件位置
     */
    private String[] locations;

    /**
     * xml解析器
     */
    private final DOMParser domParser = new DOMParser();

    /**
     * 存储bean名称
     */
    private final List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * 存储bean名称和定义的映射
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 存储bean类型和名称的映射
     */
    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<>();

    /**
     * 存储bean名称和实例的映射
     */
    private final Map<String, Object> singletonObjectMap = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) {
        // 从缓存中获取
        if (singletonObjectMap.containsKey(name)) {
            return singletonObjectMap.get(name);
        }

        // 获取bean定义
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new BeanException("bean definition not exist");
        }

        String beanName = beanDefinition.getBeanName();
        String beanClassName = beanDefinition.getBeanClassName();
        if (beanClassName == null) {
            throw new BeanException("bean class name not exist");
        }

        Class<?> beanClass;
        try {
            beanClass = Class.forName(beanClassName);
            ((GenericBeanDefinition) beanDefinition).setBeanClass(beanClass);
        } catch (Exception e) {
            throw new BeanException("bean class parse error", e);
        }

        // 创建bean对象
        Object beanInstance;
        try {
            beanInstance = beanClass.newInstance();
        } catch (Exception e) {
            throw new BeanException("bean instance create error", e);
        }

        // 注册bean
        singletonObjectMap.put(beanName, beanInstance);
        return beanInstance;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }
}
