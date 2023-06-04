package com.likejin.reggie.exception;

import com.likejin.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author 李柯锦
 * @Date 2023/6/1 15:09
 * @Description 统一异常处理
 */

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class ControllerExceptionHandler {


    /*
     * @Description 处理sql插入检查数据重复抛出异常的的异常处理器
     * @param e
     * @return R
     **/
    //这里写exception无法根据字符串获取，因为exception又封装了一层
    //exceptioin错误信息为 update error
    //SQLIntegrityConstraintViolationException错误信息为 Duplicate entry 'zhansan'
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R handleException(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());
        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            String msg =  split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("后台未知异常出错");
    }



    /*
     * @Description 自定义异常处理器
     * @param e
     * @return R
     **/
    @ExceptionHandler(MyException.class)
    public R handleMyException(MyException e){
        log.error(e.getMsg());
        return R.error(e.getMsg());
    }
}
