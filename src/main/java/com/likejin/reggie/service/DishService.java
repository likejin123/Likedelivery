package com.likejin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.likejin.reggie.dto.DishDto;
import com.likejin.reggie.entity.Dish;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:33
 * @Description
 */
public interface DishService extends IService<Dish> {

    //新增菜品同时插入菜品对应的口味数据。。需要操作两张表。。dish.dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByidWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);
}
