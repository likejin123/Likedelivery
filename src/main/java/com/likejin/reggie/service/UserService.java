package com.likejin.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.likejin.reggie.entity.User;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 18:42
 * @Description
 */
public interface UserService extends IService<User> {

    /*
     * @Description 通过id获取手机号
     * @param userId
     * @return String
     **/
    String getPhoneById(Long userId);
}
