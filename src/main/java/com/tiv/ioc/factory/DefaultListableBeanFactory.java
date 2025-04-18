package com.tiv.ioc.factory;

import com.tiv.ioc.bean.BeanDefinition;
import com.tiv.ioc.bean.BeanDefinitionReader;
import com.tiv.ioc.bean.impl.GenericBeanDefinition;
import com.tiv.ioc.bean.impl.XmlBeanDefinitionReader;
import com.tiv.ioc.exception.BeanException;
import com.tiv.ioc.io.impl.DefaultResourceLoader;

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
     * 存储bean名称
     */
    private final List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * 存储bean名称和定义信息的映射
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

    /**
     * 资源解析器
     */
    private final BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(new DefaultResourceLoader());

    /**
     * 解析xml文件构造器
     *
     * @param locations
     */
    public DefaultListableBeanFactory(String... locations) {
        this.locations = locations;
        List<BeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinitions(locations);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            // 注册bean定义信息
            beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
            beanDefinitionNames.add(beanDefinition.getBeanName());
        }
    }

    @Override
    public Object getBean(String name) {
        // 从缓存中获取
        if (singletonObjectMap.containsKey(name)) {
            return singletonObjectMap.get(name);
        }

        // 获取bean定义信息
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> requiredType) {
        String[] resolvedBeanNames = allBeanNamesByType.get(requiredType);
        if (resolvedBeanNames == null) {
            List<String> candidateBeanNames = new ArrayList<>();
            for (String beanName : beanDefinitionNames) {
                Object beanInstance = singletonObjectMap.get(beanName);
                if (beanInstance != null) {
                    if (requiredType.isInstance(beanInstance)) {
                        candidateBeanNames.add(beanName);
                    }
                } else {
                    BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                    if (beanDefinition != null) {
                        GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) beanDefinition;
                        if (genericBeanDefinition.hasBeanClass()) {
                            Class<?> beanClass = genericBeanDefinition.getBeanClass();
                            if (requiredType.isAssignableFrom(beanClass)) {
                                candidateBeanNames.add(beanName);
                            }
                        } else {
                            String beanClassName = genericBeanDefinition.getBeanClassName();
                            try {
                                Class<?> beanClass = Class.forName(beanClassName);
                                genericBeanDefinition.setBeanClass(beanClass);
                                if (requiredType.isAssignableFrom(beanClass)) {
                                    candidateBeanNames.add(beanName);
                                }
                            } catch (Exception e) {
                                throw new BeanException("bean class parse error", e);
                            }
                        }
                    }
                }
            }
            resolvedBeanNames = candidateBeanNames.toArray(new String[0]);
            allBeanNamesByType.put(requiredType, resolvedBeanNames);
        }
        if (resolvedBeanNames.length > 1) {
            throw new BeanException("more than one bean found");
        }
        return (T) getBean(resolvedBeanNames[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Object beanInstance = getBean(name);
        if (beanInstance == null) {
            throw new BeanException("bean not exist");
        }

        if (!requiredType.isInstance(beanInstance)) {
            throw new BeanException("bean type not match");
        }
        return (T) beanInstance;
    }
}
