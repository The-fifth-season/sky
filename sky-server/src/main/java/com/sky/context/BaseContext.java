package com.sky.context;

import org.springframework.stereotype.Component;

@Component
public class BaseContext {

    //通过线程栈，进行数据的储存，通过静态的变量，方便对数据进行储存和提取，并且每个线程的线程栈都是独立的，
    //不同线程间不会发生冲突，多线程时也能使用ThreadLocal这个参数来储存数据；
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> threadLocal2 = new ThreadLocal<>();


    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

    public static void setCurrentName(String name) {
        threadLocal2.set(name);
    }

    public static String getCurrentName() {
        return threadLocal2.get();
    }

    public static void removeCurrentName() {
        threadLocal2.remove();
    }
}
