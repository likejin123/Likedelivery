package com.likejin.reggie.dto;

import com.likejin.reggie.entity.Setmeal;
import com.likejin.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
