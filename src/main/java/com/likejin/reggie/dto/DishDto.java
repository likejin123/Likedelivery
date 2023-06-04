package com.likejin.reggie.dto;

import com.likejin.reggie.entity.Dish;
import com.likejin.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * @Description 针对增加菜品封装的dto
 **/
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
