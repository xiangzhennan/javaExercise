package cn.summer.core;

public interface ApplicationContext {
    public Object getBean(String beanName);
    public Object getBean(Class<?> clazz);
}
