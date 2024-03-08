package com.sky.utils;

public class RandomUtil {
    public static int getRandomInt(int min, int max) {
        // 生成一个范围在[min, max]之间的随机数
        return (int) (Math.random() * (max - min + 1)) + min;
    }
    public static long getRandomLong(long min, long max) {
        // 生成一个范围在[min, max]之间的随机数
        return (long) (Math.random() * (max - min + 1)) + min;
    }
}
