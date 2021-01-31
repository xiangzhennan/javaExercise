package cn.summer.core;

import cn.summer.annotations.Component;
import cn.summer.annotations.ComponentScan;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class AnnotationApplicationContext implements ApplicationContext {
    private Class configClass;
    private HashMap<String , Object> ioc;
    private HashMap<String, BeanDefination> beanDefinationMap;

    public AnnotationApplicationContext(Class configClass){
        //ioc and definationMap are big objects, need to be singleton
        //but lets take care of it later
        ioc = new HashMap<>();
        beanDefinationMap = new HashMap<>();
        this.configClass = configClass;
        //read all config info, create defination class for each bean
        collectBeans();
        createSingletonBeans();
    }

    private void collectBeans() {
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
        //open directory
        File file = new File(resource.getFile());
        File[] files = file.listFiles();
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
        if ("".equals(component.value())){
            String name = aClass.getName();
            String[] split = name.split("\\.");
            char[] chars = split[split.length - 1].toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return String.valueOf(chars);
        }else{
            return component.value();
        }
    }

    private void createSingletonBeans() {
        for (String beanName : beanDefinationMap.keySet()) {
            try {
                Object newBean = createNewBean(beanName);
                ioc.put(beanName,newBean);
                ioc.put(newBean.getClass().getName(),newBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private Object createNewBean(String beanName) throws IllegalAccessException, InstantiationException {
        BeanDefination beanDefination = this.beanDefinationMap.get(beanName);
        Class beanClass = beanDefination.getBeanClass();
        Object instance = beanClass.newInstance();
        return instance;
    }

    @Override
    public Object getBean(String beanName) {
        return ioc.get(beanName);
    }

    @Override
    public Object getBean(Class clazz) {
        return ioc.get(clazz.getName());
    }
}
