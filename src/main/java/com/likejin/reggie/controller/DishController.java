package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.likejin.reggie.common.R;
import com.likejin.reggie.dto.DishDto;
import com.likejin.reggie.entity.Category;
import com.likejin.reggie.entity.Dish;
import com.likejin.reggie.entity.DishFlavor;
import com.likejin.reggie.entity.SetmealDish;
import com.likejin.reggie.exception.Assert;
import com.likejin.reggie.exception.ResponseEnum;
import com.likejin.reggie.service.CategoryService;
import com.likejin.reggie.service.DishFlavorService;
import com.likejin.reggie.service.DishService;
import com.likejin.reggie.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 20:11
 * @Description 菜品管理
 */

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;


    /*
     * @Description 新增菜品
     * @param dishDto
     * @return R
     **/
    @PostMapping
    //新增菜品功能
    //有四次请求,获取菜品分类数据。。请求图片下载。。请求图片回显。。保存菜品相关数据
    public R save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /*
     * @Description 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return R
     **/
    //菜品分页查询
    //有两次请求。。展示菜品图片。。通过菜品分类id查询具体的分类名称。。
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page,
                  @RequestParam("pageSize") Integer pageSize,
                  @RequestParam(value = "name",required = false) String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        //有categoryName属性
        Page<DishDto> dishDtoPage = new Page<>();

        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.like(name!=null,"name",name);
        dishQueryWrapper.orderByAsc("sort");

        dishService.page(dishPage,dishQueryWrapper);

        //需要将categoryId字段转换为categoryName
        //即需要返回的对象多加入categoryName字段
        //对象拷贝（忽略掉records）
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        //封装dishDtoPage所需要的records
        //需要基本的dish数据
        //需要categoryName
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = new ArrayList<>();
        for(Dish dish:records){
            DishDto dishDto = new DishDto();
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            BeanUtils.copyProperties(dish,dishDto);
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /*
     * @Description 根据id查询菜品基本信息。。DTO对象
     * @param id
     * @return R
     **/
    //编辑菜品
    //菜品分类数据获取。。根据id查询菜品基本信息（包含口味）。。回显图片请求。。。保存数据请求
    @GetMapping("/{id}")
    public R getDishByid(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByidWithFlavor(id);
        return R.success(dishDto);
    }


    /*
     * @Description 修改菜品 保存数据请求
     * @param dishDto
     * @return R
     **/
    @PutMapping
    //编辑菜品
    //菜品分类数据获取。。根据id查询菜品基本信息（包含口味）。。回显图片请求。。。保存数据请求
    public R update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /*
     * @Description 根据分类id查询对应的菜品
     * @param categoryId
     * @return R
     **/
    @GetMapping("/list")
    public R list(@RequestParam("categoryId") Long categoryId){
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("category_id",categoryId);
        dishQueryWrapper.orderByAsc("sort").orderByDesc("update_time");

        //只查在售卖的，不在售卖不差.1表示在售卖
        dishQueryWrapper.eq("status",1);
        List<Dish> list = dishService.list(dishQueryWrapper);


        //扩展返回值 DishDto 因为移动端展示时需要口味数据List<DishFlavor> flavors
        List<DishDto> dishDtoList = new ArrayList<>();
        for(Dish dish:list){
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            //当前菜品Id
            Long dishId = dish.getId();
            QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
            dishFlavorQueryWrapper.eq("dish_id",dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            dishDtoList.add(dishDto);
        }
        return R.success(dishDtoList);
    }

    /*
     * @Description 菜品停售功能起售功能
     * @param value
     * @param dishId
     * @return R
     **/
    @PostMapping("/status/{value}")
    public R status(@PathVariable("value") Integer value,@RequestParam("ids") List<Long> dishIds){
        //查看菜品是否关联套餐，关联套餐无法停售（起售不用判断）
        if(value == 0){
            //select count(*) from setmeal_dish where dish_id in (?,?,?);
            QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
            setmealDishQueryWrapper.in("dish_id",dishIds);
            int count = setmealDishService.count(setmealDishQueryWrapper);
            Assert.eqZero(count, ResponseEnum.DISH_SETMEAL);
        }
        //如不关联菜品，则直接停售
        UpdateWrapper<Dish> dishUpdateWrapper = new UpdateWrapper<>();
        dishUpdateWrapper.set("status",value)
                .in("id",dishIds);
        dishService.update(dishUpdateWrapper);

        return R.success("修改成功");
    }

    @DeleteMapping
    public R delete(@RequestParam("ids") List<Long> dishIds){
        //查看菜品是否关联套餐，关联套餐无法删除
        //select count(*) from setmeal_dish where dish_id in (?,?,?);
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.in("dish_id",dishIds);
        int count = setmealDishService.count(setmealDishQueryWrapper);
        Assert.eqZero(count, ResponseEnum.DISH_SETMEAL_DELETE);

        //删除
        //delete dish where id in (?,?,?)
        dishService.removeByIds(dishIds);
        return R.success("删除成功");
    }
}
