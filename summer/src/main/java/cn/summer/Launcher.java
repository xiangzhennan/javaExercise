package cn.summer;

import cn.summer.annotations.ComponentScan;
import cn.summer.components.SummerParent;
import cn.summer.components.SummerTest;
import cn.summer.core.AnnotationApplicationContext;

@ComponentScan(value = "cn.summer.components")
public class Launcher {
    public static void main(String[] args) {
        AnnotationApplicationContext annotationApplicationContext = new AnnotationApplicationContext(Launcher.class);
        SummerParent summerParent = (SummerParent) annotationApplicationContext.getBean("summerParent");
        summerParent.summerTest.test();
    }
}
