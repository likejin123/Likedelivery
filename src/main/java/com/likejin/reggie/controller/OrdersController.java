package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.likejin.reggie.common.BaseContext;
import com.likejin.reggie.common.R;
import com.likejin.reggie.dto.OrdersDto;
import com.likejin.reggie.entity.OrderDetail;
import com.likejin.reggie.entity.Orders;
import com.likejin.reggie.service.OrderDetailService;
import com.likejin.reggie.service.OrderService;
import com.likejin.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 21:55
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /*
     * @Description 用户下单
     * @param orders
     * @return R
     **/
    //去结算。。订单确认。。
    // 获取当前登录用户的默认地址。。请求当前购物车的明细。。
    @PostMapping("/submit")
    public R submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /*
     * @Description 获取订单分页数据
     * @param page
     * @param pageSize
     * @return R
     **/
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.orderByDesc("order_time");
        orderService.page(ordersPage,ordersQueryWrapper);
        return R.success(ordersPage);
    }

    /*
     * @Description 修改订单状态，如果完成给用户发短信
     * @param orders
     * @return R
     **/
    @PutMapping
    public R status(@RequestBody Orders orders){
        UpdateWrapper<Orders> ordersUpdateWrapper = new UpdateWrapper<>();
        ordersUpdateWrapper.eq("id",orders.getId())
                .set("status",orders.getStatus());
        orderService.update(ordersUpdateWrapper);

        //判断如果订单已完成。。给对应user发送短信告诉订单完成。。
        Orders orders1 = orderService.getById(orders.getId());
        Long userId = orders1.getUserId();

        if(orders.getStatus().intValue() == 4){
            String phone = userService.getPhoneById(userId);
            //获取手机号后发送短信为通知送达
            //SMSUtils.sendMessage(SMSUtils.signName,SMSUtils.templateCode,phone,"6666");TODO（可以发送短信）
            log.info("code{}","6666");

        }
        return R.success("更新状态成功");
    }


    //查询orders的分页数据（用户端）
    // records = orderlist

    //返回Page<OrderDto>
    //其中OrderDto包含Order的数据 包含OrderDetail的数据/userPage?page=1&pageSize=1
    /*
     * @Description 获取order的详细数据（orders + ordersDetails）
     * @param page
     * @param pageSize
     * @return R
     **/
    @Transactional
    @GetMapping("/userPage")
    public R userPage(@RequestParam("page") Integer page,
                      @RequestParam("pageSize") Integer pageSize){
        //查出对应用户的订单 （order_time的降序）
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("user_id", BaseContext.getCurrentId())
                .orderByDesc("order_time");
        List<Orders> list = orderService.list(ordersQueryWrapper);
        //封装OrderDto分页数据，包含每一个订单的orderDetails
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);;
        BeanUtils.copyProperties(ordersPage,ordersDtoPage,"records");
        ArrayList<OrdersDto> ordersDtos = new ArrayList<>();
        for(Orders orders: list){
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders,ordersDto);
            //根据每一个orders的id查询对应的orderDetails
            QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
            orderDetailQueryWrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailQueryWrapper);
            ordersDto.setOrderDetails(orderDetailList);
            ordersDtos.add(ordersDto);
        }
        ordersDtoPage.setRecords(ordersDtos);
        return R.success(ordersDtoPage);
    }

}
