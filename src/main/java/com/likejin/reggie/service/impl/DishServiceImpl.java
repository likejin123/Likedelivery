package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.dto.DishDto;
import com.likejin.reggie.entity.Dish;
import com.likejin.reggie.entity.DishFlavor;
import com.likejin.reggie.mapper.DishMapper;
import com.likejin.reggie.service.DishFlavorService;
import com.likejin.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:33
 * @Description
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;
    /*
     * @Description 新增菜品并且保存对应的口味数据
     * @param null
     * @return null
     **/

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到dish表
        this.save(dishDto);

        //获取dish_flavor对应的dish_id
        //id如何获取到的？猜测上面的service为dishDto自动生成了id并保存了下来
        Long dishId = dishDto.getId();

        //获取菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor dishFlavor:flavors){
            dishFlavor.setDishId(dishId);
        }

        //保存菜品口味数据到口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /*
     * @Description 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return DishDto
     **/
    @Override
    public DishDto getByidWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        //查询菜品基本信息
        Dish dish = this.getById(id);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        dishFlavorQueryWrapper.eq("dish_id",id);
        List<DishFlavor> list = dishFlavorService.list(dishFlavorQueryWrapper);

        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /*
     * @Description 更新菜品以及更新菜品口味
     * @param dishDto
     * @return void
     **/
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        //更新dish表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        this.updateById(dish);

        //清理当前菜品对应的口味数据
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        dishFlavorQueryWrapper.eq("dish_id",dishDto.getId());
        dishFlavorService.remove(dishFlavorQueryWrapper);

        //添加当前提交过来的口味数据 insert操作 dish_id没有值（新增的就没有dish_id了原来的有dish_id）
        Long dishId = dishDto.getId();
        //获取菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor dishFlavor:flavors){
            dishFlavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(dishDto.getFlavors());


    }
}
