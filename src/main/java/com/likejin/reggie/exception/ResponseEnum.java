package com.likejin.reggie.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 李柯锦
 * @Date 2023/6/1 15:05
 * @Description 异常类出现的错误
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    //根据全参注入
    EMPLOYEE_DUPLICATE(0,"用户已经被添加"),
    EMPLOYE_NOTFOUND(0,"用户未找到"),

    CATEGORY_ERROR(0,"新增分类失败"),
    CATEGORY_DUPLICATE(0,"当前分类关联信息，无法删除" ),

    SETMEAL_SALE(0,"套餐正在售卖无法删除" ),

    SHOPPINGCART_NULL(0,"购物车为空无法下单" ),

    DISH_SETMEAL(0,"已有套餐包含菜品，无法停售" ),

    DISH_SETMEAL_DELETE(0,"已有套餐包含菜品，无法删除" );

    //默认错误码code为0
    private Integer code;
    private String msg;
}
