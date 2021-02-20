package cn.summer.components;

import cn.summer.annotations.AutoWired;
import cn.summer.annotations.Component;

@Component
public class SummerTest {

    @AutoWired
    public SummerParent summerParent;
    public void test(){
        System.out.println("hello summer from test");
    }
}
