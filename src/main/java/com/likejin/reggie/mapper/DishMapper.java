package com.likejin.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.likejin.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:32
 * @Description
 */

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
