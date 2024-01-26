package com.sky.aop;

import lombok.Getter;

public class Aspect1 {
    @Getter
    private static String string;

    public static void setString(String string) {
        Aspect1.string = string;
    }
}
