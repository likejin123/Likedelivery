package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.likejin.reggie.common.R;
import com.likejin.reggie.dto.SetmealDto;
import com.likejin.reggie.entity.Category;
import com.likejin.reggie.entity.Setmeal;
import com.likejin.reggie.entity.SetmealDish;
import com.likejin.reggie.service.CategoryService;
import com.likejin.reggie.service.SetmealDishService;
import com.likejin.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 15:23
 * @Description
 */


@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    //新增套餐的交互过程
    //获取套餐分类数据。。获取菜品分离数据。。
    //根据菜品分类id查询对应菜品数据。。
    //图片上传。。图片下载。。
    //保存套餐数据。。（setmeal,setmeal_dish）


    /*
     * @Description 新增套餐
     * @param setmealDto
     * @return R
     **/
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }


    /*
     * @Description 分页展示套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return R
     **/
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page,
                  @RequestParam("pageSize") Integer pageSize,
                  @RequestParam(value = "name",required = false) String name){
            //套餐 + categoryName
        //查询Setmeal的分页数据
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.like(name!=null,"name",name);
        setmealService.page(setmealPage, setmealQueryWrapper);

        //封装前端需要的setmealDto
        Page<SetmealDto> setmealDtoPage = new Page<>();
        List<SetmealDto> records = new ArrayList<SetmealDto>();
        //除了records其他数据先拷贝
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        for(Setmeal setmeal:setmealPage.getRecords()){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            records.add(setmealDto);
        }
        setmealDtoPage.setRecords(records);
        return R.success(setmealDtoPage);
    }

    //先停售，才能删除套餐
    /*
     * @Description 删除套餐
     * @param ids
     * @return R
     **/
    @DeleteMapping
    public R delete(@RequestParam("ids") List<Long> ids){

        //删除套餐表
        //删除对应的套餐菜品关联关系表
        setmealService.removeWithDish(ids);
        return R.success("成功删除");
    }



    @GetMapping("/list")
    public R list(@RequestParam("categoryId") String categoryId,
                  @RequestParam("status") String status){

        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.eq("category_id",categoryId)
                .eq("status",status)
                .orderByDesc("update_time");
        List<Setmeal> list = setmealService.list(setmealQueryWrapper);
        return R.success(list);
    }


    /*
     * @Description 套餐停售功能起售功能
     * @param value
     * @param dishId
     * @return R
     **/
    @PostMapping("/status/{value}")
    public R status(@PathVariable("value") Integer value,@RequestParam("ids") List<Long> dishIds){
        UpdateWrapper<SetmealDish> dishUpdateWrapper = new UpdateWrapper<>();
        dishUpdateWrapper.set("status",value)
                .in("id",dishIds);
        setmealDishService.update(dishUpdateWrapper);
        return R.success("修改成功");
    }

    /*
     * @Description 编辑套餐时回显套餐
     * @param setmealId
     * @return R
     **/
    @GetMapping("/{id}")
    public R get(@PathVariable("id") Long setmealId){

        SetmealDto setmealDto = new SetmealDto();
        //查询id对应的套餐setmeal
        Setmeal setmeal = setmealService.getById(setmealId);

        //查询id对应的套餐菜品setmeal_dish
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.eq("setmeal_id",setmealId);
        List<SetmealDish> list = setmealDishService.list(setmealDishQueryWrapper);

        BeanUtils.copyProperties(setmeal,setmealDto,"setmealDishes");
        setmealDto.setSetmealDishes(list);

        return R.success(setmealDto);

    }

    /*
     * @Description 修改套餐
     * @param setmealDto
     * @return R
     **/
    @PutMapping
    public R edit(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("编辑套餐成功");
    }


}
