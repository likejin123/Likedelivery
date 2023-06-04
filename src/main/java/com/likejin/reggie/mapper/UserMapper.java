package com.likejin.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.likejin.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 18:41
 * @Description
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
