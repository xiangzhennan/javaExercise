package cn.summer.components;

import cn.summer.annotations.AutoWired;
import cn.summer.annotations.Component;

@Component
public class SummerParent {
    @AutoWired
    public SummerTest summerTest;
}
