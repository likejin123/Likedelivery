package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.dto.SetmealDto;
import com.likejin.reggie.entity.Setmeal;
import com.likejin.reggie.entity.SetmealDish;
import com.likejin.reggie.exception.Assert;
import com.likejin.reggie.exception.ResponseEnum;
import com.likejin.reggie.mapper.SetmealMapper;
import com.likejin.reggie.service.SetmealDishService;
import com.likejin.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:35
 * @Description
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /*
     * @Description 新增套餐同时保存套餐和菜品的关联关系（兼容编辑）
     * @param setmealDto
     * @return void
     **/

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        //保存套餐的基本信息。setmeal的insert
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        setmeal.setStatus(1);

        //如果已经有了该套餐则保存，没有则添加
        Setmeal setmealId = this.getById(setmeal.getId());
        if(setmealId!=null){
            this.updateById(setmealId);
        }else{
            this.save(setmeal);
        }

        //为了兼容编辑。。即先删除有的setmeal_dish
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.eq("setmeal_id",setmealDto.getId());
        setmealDishService.remove(setmealDishQueryWrapper);

        //保存套餐和菜品的关联关系。对setmeal_dish执行insert
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for(SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    /*
     * @Description 删除套餐同时删除套餐菜品的关联数据 setmeal setmeal_dish
     * @param ids
     * @return void
     **/
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态确定是否可以删除
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.eq("status",1)
                .in("id",ids);
        int count = this.count(setmealQueryWrapper);

        //如果不能删除，抛出一个业务异常
        Assert.eqZero(count, ResponseEnum.SETMEAL_SALE);


        //如果可以删除 先删除套餐表中的数据
        this.removeByIds(ids);

        //删除关系表单的数据setmeal_dish
        //delete from setmeal_dish where setmeal_id in (1,2,3)
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.in("setmeal_id",ids);
        setmealDishService.remove(setmealDishQueryWrapper);

    }
}
