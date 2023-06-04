package com.likejin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.likejin.reggie.entity.Category;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:02
 * @Description
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
