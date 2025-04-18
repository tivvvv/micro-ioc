package com.tiv.ioc.bean.impl;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.tiv.ioc.bean.BeanDefinition;
import com.tiv.ioc.bean.BeanDefinitionReader;
import com.tiv.ioc.exception.BeanException;
import com.tiv.ioc.io.Resource;
import com.tiv.ioc.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.beans.Introspector;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于xml的bean定义信息读取器
 */
public class XmlBeanDefinitionReader implements BeanDefinitionReader {

    /**
     * 资源加载器
     */
    private ResourceLoader resourceLoader;

    /**
     * xml解析器
     */
    private DOMParser domParser = new DOMParser();

    public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    @Override
    public List<BeanDefinition> loadBeanDefinitions(String location) {
        ResourceLoader resourceLoader = getResourceLoader();
        if (resourceLoader == null) {
            throw new BeanException("no resourceLoader available");
        }
        Resource resource = resourceLoader.getResource(location);
        return loadBeanDefinitions(resource);
    }

    @Override
    public List<BeanDefinition> loadBeanDefinitions(String... locations) {
        List<BeanDefinition> result = new ArrayList<>();
        for (String location : locations) {
            List<BeanDefinition> beanDefinitions = loadBeanDefinitions(location);
            result.addAll(beanDefinitions);
        }
        return result;
    }

    @Override
    public List<BeanDefinition> loadBeanDefinitions(Resource resource) {
        InputStream inputStream = resource.getInputStream();
        Document document;
        try {
            domParser.parse(new InputSource(inputStream));
            document = domParser.getDocument();
            domParser.dropDocumentReferences();
        } catch (Exception e) {
            throw new BeanException("document parse error", e);
        }

        if (document == null) {
            throw new BeanException("document location not exist");
        }

        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element && "bean".equals(node.getNodeName())) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String beanClassName = element.getAttribute("class");
                if (beanClassName.isEmpty()) {
                    throw new BeanException("bean class name not exist");
                }

                if (id.isEmpty()) {
                    id = generateBeanName(beanClassName);
                }

                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanName(id);
                beanDefinition.setBeanClassName(beanClassName);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;
    }

    @Override
    public List<BeanDefinition> loadBeanDefinitions(Resource... resources) {
        List<BeanDefinition> result = new ArrayList<>();
        for (Resource resource : resources) {
            List<BeanDefinition> beanDefinitions = loadBeanDefinitions(resource);
            result.addAll(beanDefinitions);
        }
        return result;
    }

    /**
     * 生成bean名称
     *
     * @param className
     * @return
     */
    private String generateBeanName(String className) {
        int nameEndIndex = className.indexOf("$$");
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }

        int lastDotIndex = className.lastIndexOf(46);
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace('$', '.');

        return Introspector.decapitalize(shortName);
    }
}
