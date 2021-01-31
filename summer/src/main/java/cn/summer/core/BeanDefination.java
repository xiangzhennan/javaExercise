package cn.summer.core;

/*
* bean is not always as easy as a class without any fields,
* so a defination class is needed to record important information
* */

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class BeanDefination {
    private String scope;
    private Class beanClass;
}
