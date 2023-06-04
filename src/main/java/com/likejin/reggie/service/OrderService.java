package com.likejin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.likejin.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    /*
     * @Description 用户下单
     * @param orders
     * @return void
     **/
    public void submit(Orders orders);
}
