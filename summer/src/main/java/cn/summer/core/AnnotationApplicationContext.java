package cn.summer.core;

import cn.summer.annotations.Component;
import cn.summer.annotations.ComponentScan;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class AnnotationApplicationContext implements ApplicationContext {

    private HashMap<String , Object> ioc;

    public AnnotationApplicationContext(Class configClass){
        //ioc is a big object, need to be singleton
        //but lets take care of it later
        ioc = new HashMap<>();
        initContainer(ioc, configClass);
    }

    private void initContainer(HashMap<String, Object> ioc, Class configClass) {
        String basePackage;
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        if (componentScan == null){
            System.out.println("no base package set");
            return;
        }else{
            basePackage = componentScan.value();
            basePackage = basePackage.replace(".", "/");
            System.out.println("base package is "+ basePackage);
        }
        ClassLoader classLoader = AnnotationApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(basePackage);
        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        for (File f : files) {
            String absolutePath = f.getAbsolutePath();
            absolutePath = absolutePath.substring(absolutePath.indexOf("cn"),absolutePath.indexOf(".class"));
            absolutePath = absolutePath.replace("\\",".");
            try {
                Class<?> aClass = classLoader.loadClass(absolutePath);
                if (aClass.isAnnotationPresent(Component.class)){
                    Component component = aClass.getAnnotation(Component.class);
                    String beanName = component.value();
                    System.out.println(beanName);
                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public Object getBean(String beanName) {
        return ioc.get(beanName);
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return ioc.get(clazz.getName());
    }
}
