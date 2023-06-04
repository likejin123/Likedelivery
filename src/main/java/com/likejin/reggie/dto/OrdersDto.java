package com.likejin.reggie.dto;

import com.likejin.reggie.entity.OrderDetail;
import com.likejin.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
