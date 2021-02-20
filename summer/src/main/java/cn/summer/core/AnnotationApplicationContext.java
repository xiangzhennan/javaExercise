package cn.summer.core;

import cn.summer.annotations.AutoWired;
import cn.summer.annotations.Component;
import cn.summer.annotations.ComponentScan;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;

public class AnnotationApplicationContext implements ApplicationContext {
    private Class configClass;
    private HashMap<String, Object> singletonObjectMap;
    private HashMap<String, Object> instanceMap;
    private HashMap<String, BeanDefination> beanDefinationMap;

    public AnnotationApplicationContext(Class<?> configClass){
        singletonObjectMap = new HashMap<>();
        beanDefinationMap = new HashMap<>();
        instanceMap = new HashMap<>();
        this.configClass = configClass;
        //read all config info, create defination class for each bean
        collectBeanDefinations();
    }
//    scan base package, create all BeanDefinations
    private void collectBeanDefinations() {
        String basePackage;
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        if (componentScan == null){
            System.out.println("no base package set");
            return;
        }else{
            basePackage = componentScan.value();
            basePackage = basePackage.replace(".", "/");
//            System.out.println("base package is "+ basePackage);
        }
        ClassLoader classLoader = AnnotationApplicationContext.class.getClassLoader();
        //get application class path, which lead to base package
        URL resource = classLoader.getResource(basePackage);
        //base package not found
        if (resource == null){
            return ;
        }
        //open directory
        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        //no class file existing
        if (files == null){
            return;
        }
        for (File f : files) {
            String absolutePath = f.getAbsolutePath();
            absolutePath = absolutePath.substring(absolutePath.indexOf("cn"),absolutePath.indexOf(".class"));
            //double \ for 转义，谁来告诉我转义英文咋说。
            absolutePath = absolutePath.replace("\\",".");
            try {
                Class<?> aClass = classLoader.loadClass(absolutePath);
                if (aClass.isAnnotationPresent(Component.class)){
                    Component component = aClass.getAnnotation(Component.class);
                    String beanName = getBeanNameOrDefault(aClass, component);
                    //default singleton, and lets take care of prototype later
                    BeanDefination beanDefination = BeanDefination.builder().scope("singleton").beanClass(aClass).build();
                    beanDefinationMap.put(beanName,beanDefination);
                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }

    }

    private String getBeanNameOrDefault(Class<?> aClass, Component component) {
        //default use classname but lower case for first letter
        if (component == null||"".equals(component.value())){
            String name = aClass.getName();
            String[] split = name.split("\\.");
            char[] chars = split[split.length - 1].toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return String.valueOf(chars);
        }else{
            return component.value();
        }
    }


    private Object createBean(String beanName) {
        BeanDefination beanDefination = beanDefinationMap.get(beanName);
        //if found instance in cache
        if (instanceMap.containsKey(beanName)){
            Object instance = instanceMap.get(beanName);
            instanceMap.remove(beanName);
            singletonObjectMap.put(beanName,instance);
            singletonObjectMap.put(instance.getClass().getName(),instance);
            return instance;
        }
        //not in cache, new instance
        Class<?> beanClass = beanDefination.getBeanClass();
        try {
            Object instance = beanClass.getDeclaredConstructor().newInstance();
            //put into cache
            instanceMap.put(beanName,instance);
            populate(instance,beanDefination);
            instanceMap.remove(beanName);
            singletonObjectMap.put(beanName,instance);
            singletonObjectMap.put(instance.getClass().getName(),instance);
            return instance;
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populate(Object instance, BeanDefination beanDefination) {
        Class<?> beanClass = beanDefination.getBeanClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field :fields) {
            //if need injection, get bean from ioc, or create new bean
            if (field.isAnnotationPresent(AutoWired.class)){
                Object filedBean = getBean(field.getName());
                if (filedBean == null){
                    filedBean = createBean(field.getName());
                }
                //inject by type
                field.setAccessible(true);
                try {
                    field.set(instance,filedBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(String beanName) {
        //priority, get from ioc container
        Object o = singletonObjectMap.get(beanName);
        if (o == null){
            return createBean(beanName);
        }
        return o;
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return getBean(clazz.getName());
    }
}
