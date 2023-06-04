package com.likejin.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 10:24
 * @Description 对数据库公共字段统一填充
 */


@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /*
     * @Description 插入操作自动填充
     * @param metaObject
     * @return void
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        //metaObject是什么 需要填充时的Object对象
        metaObject.setValue("createTime", LocalDateTime.now());
        //判断是否有该属性。有则插入。
        Object originalObject = metaObject.getOriginalObject();
        try {
            //有该字段进行赋值
            originalObject.getClass().getDeclaredField("updateTime");
            metaObject.setValue("updateTime", LocalDateTime.now());

            originalObject.getClass().getDeclaredField("createUser");
            originalObject.getClass().getDeclaredField("updateUser");
            metaObject.setValue("createUser",BaseContext.getCurrentId());
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
        } catch (NoSuchFieldException e) {
            //没有该字段不处理

        }




    }

    /*
     * @Description 更新操作自动填充
     * @param metaObject
     * @return void
     **/
    @Override
    public void updateFill(MetaObject metaObject) {

        long id = Thread.currentThread().getId();
        log.info("线程id为{}",id);
        metaObject.setValue("createTime", LocalDateTime.now());
        Object originalObject = metaObject.getOriginalObject();
        try {
            originalObject.getClass().getDeclaredField("updateTime");
            //有该字段进行赋值
            metaObject.setValue("updateTime", LocalDateTime.now());
        } catch (NoSuchFieldException e) {
            //没有该字段不处理
        }


        //如何解决获取session的employee key对应的value
        //使用ThreadLocal
        //线程的局部变量。。。ThreadLocal为每个线程提供一份单独的存储空间。。具有线程隔离效果。。
        //set设置值 get拿到值
        //客户端每次发送一次http请求，服务器都会分配一个新的线程来处理
        //如 Filter controller MetaObjectHandler都是一个线程

        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
