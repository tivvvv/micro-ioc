package com.tiv.ioc.bean;

import com.tiv.ioc.exception.BeanException;

/**
 * 通用bean定义信息类
 */
public class GenericBeanDefinition implements BeanDefinition {

    /**
     * bean名称
     */
    private String beanName;

    /**
     * bean类型
     */
    private Object beanClass;

    public Class<?> getBeanClass() {
        if (this.beanClass == null) {
            throw new BeanException("beanClass is null");
        }

        if (!(this.beanClass instanceof Class)) {
            throw new BeanException(("beanClass has not been resolved into an actual class"));
        }

        return (Class<?>) this.beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean hasBeanClass() {
        return this.beanClass instanceof Class;
    }


    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getBeanClassName() {
        if (this.beanClass instanceof Class) {
            return ((Class<?>) this.beanClass).getName();
        }
        return (String) this.beanClass;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }
}
