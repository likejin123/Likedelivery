package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.likejin.reggie.common.R;
import com.likejin.reggie.entity.User;
import com.likejin.reggie.service.UserService;
import com.likejin.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author 李柯锦
 * @Date 2023/6/3 18:43
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    /*
     * @Description 手机发送验证码
     * @param user
     * @return R
     **/
    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user, HttpServletRequest request){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //阿里云短信服务发送短信
//            SMSUtils.sendMessage(SMSUtils.signName,SMSUtils.templateCode,phone,code);TODO(可以发送短信)
            log.info("code{}",code);
            //保存验证码到session，
            HttpSession session = request.getSession();
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R login(@RequestBody Map map, HttpServletRequest request){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //比对验证码（session获取验证码）
        String codeInSession = request.getSession().getAttribute(phone).toString();
        //比对成功，说明登录成功
        if(codeInSession!=null && codeInSession.equals(code)){
            //判断手机对应用户是否为新用户
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("phone",phone);
            User user = userService.getOne(userQueryWrapper);
            if(user == null){
                //如果是新用户自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            request.getSession().setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");

    }

}
