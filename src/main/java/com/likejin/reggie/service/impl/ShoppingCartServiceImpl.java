package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.entity.ShoppingCart;
import com.likejin.reggie.mapper.ShoppingCartMapper;
import com.likejin.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 21:20
 * @Description
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {
}
