package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.entity.User;
import com.likejin.reggie.mapper.UserMapper;
import com.likejin.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 18:42
 * @Description
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{

    /*
     * @Description 通过id获取手机号
     * @param userId
     * @return String
     **/
    @Override
    public String getPhoneById(Long userId) {
        User user = this.getById(userId);
        return user.getPhone();
    }
}
