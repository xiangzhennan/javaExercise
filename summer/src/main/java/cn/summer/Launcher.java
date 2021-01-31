package cn.summer;

import cn.summer.annotations.ComponentScan;
import cn.summer.components.SummerTest;
import cn.summer.core.AnnotationApplicationContext;

@ComponentScan(value = "cn.summer.components")
public class Launcher {
    public static void main(String[] args) {
        AnnotationApplicationContext annotationApplicationContext = new AnnotationApplicationContext(Launcher.class);
        SummerTest summerTest = (SummerTest)annotationApplicationContext.getBean("summerTest");
        summerTest.test();
    }
}
