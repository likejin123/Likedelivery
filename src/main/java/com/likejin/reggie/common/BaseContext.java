package com.likejin.reggie.common;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 10:44
 * @Description 基于ThreadLocal封装工具类。用于保存和获取当前登录用户的id(同一个线程内)
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /*
     * @Description 设置值
     * @param id
     * @return void
     **/
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /*
     * @Description 获取值
     * @param
     * @return Long
     **/
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
