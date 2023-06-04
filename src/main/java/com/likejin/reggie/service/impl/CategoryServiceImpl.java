package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.entity.Category;
import com.likejin.reggie.entity.Dish;
import com.likejin.reggie.entity.Setmeal;
import com.likejin.reggie.exception.Assert;
import com.likejin.reggie.exception.ResponseEnum;
import com.likejin.reggie.mapper.CategoryMapper;
import com.likejin.reggie.service.CategoryService;
import com.likejin.reggie.service.DishService;
import com.likejin.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:02
 * @Description
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {


    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /*
     * @Description 根据id删除分类。删除之前判断
     * @param id
     * @return void
     **/
    public void remove(Long id){
        //查询当前分类是否关联菜品，如果关联则抛出业务异常

        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("category_id",id);

        int count = dishService.count(dishQueryWrapper);
        Assert.eqZero(count, ResponseEnum.CATEGORY_DUPLICATE);

        //查询当前分类是否关联套餐，如果关联抛出业务异常

        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.eq("category_id",id);

        int count1 = dishService.count(dishQueryWrapper);
        Assert.eqZero(count1, ResponseEnum.CATEGORY_DUPLICATE);

        //正常删除
        super.removeById(id);
    }
}
