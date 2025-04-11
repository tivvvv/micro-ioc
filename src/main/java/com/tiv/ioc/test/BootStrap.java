package com.tiv.ioc.test;

import com.tiv.ioc.factory.DefaultListableBeanFactory;

public class BootStrap {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory("application.xml");
        Object student1 = beanFactory.getBean("student");
        Student student2 = beanFactory.getBean(Student.class);
        Student student3 = beanFactory.getBean("student", Student.class);

        System.out.println(student1);
        System.out.println(student2);
        System.out.println(student3);
    }
}
