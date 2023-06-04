package com.likejin.reggie.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author 李柯锦
 * @Date 2023/6/1 15:13
 * @Description
 */


@Slf4j
public class Assert {

    /*
     * @Description 判断sql执行是否成功
     * @param isSuccess
     * @param responseEnum
     * @return void
     **/
    public static void SQLIsSuccess(boolean isSuccess,ResponseEnum responseEnum){
        if(!isSuccess){
            log.error("sql执行失败");
            throw new MyException(responseEnum);
        }
    }

    /*
     * @Description 判断对象是否为null
     * @param obj
     * @param responseEnum
     * @return void
     **/
    public static void isNotNull(Object obj,ResponseEnum responseEnum){
        if(obj==null){
            throw new MyException(responseEnum);
        }
    }


    /*
     * @Description 判断表达式是否为true
     * @param isTrue
     * @param responseEnum
     * @return void
     **/
    public static void isTrue(boolean isTrue,ResponseEnum responseEnum){
        if(!isTrue){
            throw  new MyException(responseEnum);
        }
    }


    /*
     * @Description 断言数量等于0
     * @param count
     * @param responseEnum
     * @return void
     **/
    public static void eqZero(Integer count,ResponseEnum responseEnum){
        if(count != 0 ){
            throw new MyException(responseEnum);
        }
    }
}
