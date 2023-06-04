package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.likejin.reggie.common.BaseContext;
import com.likejin.reggie.common.R;
import com.likejin.reggie.entity.ShoppingCart;
import com.likejin.reggie.service.DishService;
import com.likejin.reggie.service.SetmealService;
import com.likejin.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 21:21
 * @Description
 */

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {


    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    //菜品加入购物车
    /*
     * @Description 添加购物车
     * @param shoppingCart
     * @return R
     **/
    @PostMapping("/add")
    public R add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){

        //设置用户id，指定当前是哪个用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        //连续点击两次菜品加入。。应该更改number+1
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();

        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",userId);
        if(dishId != null){
            //添加到购物车的是菜品
            //查询当前菜品是否在购物车中（user_id 和 dish_id）
            shoppingCartQueryWrapper.eq("dish_id",dishId);
        }else{
            //添加到购物车的是套餐
            shoppingCartQueryWrapper.eq("setmeal_id",shoppingCart.getSetmealId());
        }
        //查询当前菜品或者套餐是否在购物车中
        //select * from shopping_cart where user_id =? and dish_id = ?/setmeal_id = ?
        ShoppingCart cartOne = shoppingCartService.getOne(shoppingCartQueryWrapper);

        if(cartOne != null){
            //如果已经存在。。在原来的数量基础上+1
            Integer number = cartOne.getNumber();
            cartOne.setNumber(number + 1);
            shoppingCartService.updateById(cartOne);
        }else {
            //如果不存在，那么新增购物车，数量默认就是1
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cartOne = shoppingCart;

        }

        return R.success(cartOne);
    }
    //点击购物车图片查看购物车的菜品和套餐

    /*
     * @Description 查看购物车
     * @param request
     * @return R
     **/
    @GetMapping("list")
    public R list(){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getCurrentId())
                .orderByAsc("create_time");
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartQueryWrapper);
        return R.success(list);
    }
    //点击清空购物车，清空对应id的购物车内容
    /*
     * @Description 清空购物车
     * @param
     * @return R
     **/
    @DeleteMapping("/clean")
    public R clean(){
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",BaseContext.getCurrentId());
        shoppingCartService.remove(shoppingCartQueryWrapper);
        return R.success("清空购物车成功");
    }


    /*
     * @Description 删除购物车中某一个商品
     * @param shoppingCart
     * @return R
     **/
    @PostMapping("/sub")
    public R sub(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",userId);
        //如果是套餐则根据套餐和user_id删除购物车
        if(shoppingCart.getDishId()!=null){
            //如果是菜品则根据菜品和user_id删除购物车
            shoppingCartQueryWrapper.eq("dish_id",shoppingCart.getDishId());
        }else{
            //如果是套餐则根据套餐和user_id删除购物车
            shoppingCartQueryWrapper.eq("setmeal_id",shoppingCart.getSetmealId());
        }
        shoppingCartService.remove(shoppingCartQueryWrapper);
        return R.success("删除成功");
    }

}
