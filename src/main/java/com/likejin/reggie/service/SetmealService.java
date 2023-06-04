package com.likejin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.likejin.reggie.dto.SetmealDto;
import com.likejin.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 11:35
 * @Description
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
