package com.likejin.reggie.exception;

import lombok.Data;

/**
 * @Author 李柯锦
 * @Date 2023/6/1 15:04
 * @Description 自定义异常
 */



@Data
public class MyException extends RuntimeException {
    public String msg;
    public Integer code;

    public MyException(ResponseEnum responseEnum){
        this.msg = responseEnum.getMsg();
        this.code = responseEnum.getCode();
    }



}
