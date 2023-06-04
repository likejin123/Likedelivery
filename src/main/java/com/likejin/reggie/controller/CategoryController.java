package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.likejin.reggie.common.R;
import com.likejin.reggie.entity.Category;
import com.likejin.reggie.exception.Assert;
import com.likejin.reggie.exception.ResponseEnum;
import com.likejin.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:03
 * @Description
 */


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /*
     * @Description 新增分类
     * @param category
     * @return R
     **/
    @PostMapping
    public R save(@RequestBody Category category){
        boolean save = categoryService.save(category);
        Assert.isTrue(save, ResponseEnum.CATEGORY_ERROR);
        return R.success("新增分类成功");
    }


    /*
     * @Description 获取分类分页
     * @param page
     * @param pageSize
     * @return R
     **/
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page,
                  @RequestParam("pageSize") Integer pageSize){
        //设置分页构造器。。按照什么分页
         Page<Category> catePage = new Page(page, pageSize);


        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.orderByAsc("sort");
        categoryService.page(catePage,categoryQueryWrapper);
         return R.success(catePage);
    }


    /*
     * @Description 根据id删除对应分类
     * @param ids
     * @return R
     **/
    @DeleteMapping
    public R delete(@RequestParam("ids") Long ids){
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }


    /*
     * @Description 根据id更新分类
     * @param category
     * @return R
     **/
    @PutMapping
    public R put(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }


    /*
     * @Description 根据条件来查询分类数据
     * @param category
     * @return R
     **/
    @GetMapping("/list")
    public R list(Category category){
        //构造器
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq(category.getType()!=null,"type",category.getType());
        categoryQueryWrapper.orderByAsc("sort").orderByDesc("update_time");

        List<Category> list = categoryService.list(categoryQueryWrapper);
        return R.success(list);
    }

}
