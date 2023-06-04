package com.likejin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.likejin.reggie.common.R;
import com.likejin.reggie.entity.Employee;
import com.likejin.reggie.exception.Assert;
import com.likejin.reggie.exception.ResponseEnum;
import com.likejin.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 李柯锦
 * @Date 2023/5/31 14:46
 * @Description
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //请求方式是post 数据放在请求体中
    @PostMapping("/login")
    /*
     * @Description 员工登录
     * @param employee
     * @param request
     * @return R
     **/
    public R login(@RequestBody Employee employee, HttpServletRequest request){
        //把员工对象的id存到session一份，可以随时获取登录对象

        ///1.页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2.根据页面提交的用户名username查询数据库
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("username",employee.getUsername());

        Employee emp = employeeService.getOne(employeeQueryWrapper);

        //3.如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败，没有该用户");
        }

        //4.查到结果后对比md5加密后的密码是否相同
        if(!emp.getPassword().equals(password )){
            //密码不相同时时
            return R.error("登陆失败，密码错误");
        }
        //5.查看员工状态是否被锁定
        if(emp.getStatus() == 0){
            return R.error("登陆失败，员工锁定");
        }
        //6.登录成功，将员工id存入session返回登录成功后的结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    /*
     * @Description 员工退出
     * @param request
     * @return R
     **/
    @PostMapping("/logout")
    public R logout(HttpServletRequest request){
        //清理session中保存的当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }



    /*
     * @Description 新增员工
     * @param employee
     * @return R
     **/
    @PostMapping
    public R save(@RequestBody Employee employee, HttpServletRequest request){

        log.info("新增员工，员工信息{}",employee);
        //统一给初始密码123456 md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        //手动设置时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//
//        //获得当前登录用户id
//        Long id = (Long)request.getSession().getAttribute("employee");
//
//        //设置创建人
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);

        //调用save方法保存
        //employeeService.save(employee);
        //id是save方法自动生成的id
        employeeService.save(employee);
        //mysql限制username只能唯一

        //注意，此处无法用Assert,因为在未得到结果就已经抛出异常了
        //Assert.SQLIsSuccess(, ResponseEnum.EMPLOYEE_DUPLICATE);

        return R.success("新增员工成功");
    }


    //http://localhost:8080/employee/page?page=1&pageSize=10（查询分页数据）
    //http://localhost:8080/employee/page?page=1&pageSize=10&name=asd（查找具体name）
    /*
     * @Description 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return R
     **/
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize,
                  //如果这里不写required = false，默认为true,则匹配进入不了方法page
                  @RequestParam(value = "name",required = false) String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        QueryWrapper queryWrapper = new QueryWrapper<Employee>();
        queryWrapper.like(StringUtils.isNotEmpty(name),"name",name);

        //添加排序条件
        queryWrapper.orderByDesc("update_time");

        //查询
        employeeService.page(pageInfo,queryWrapper);

        //注意：返回的是page对象
        return R.success(pageInfo);

    }
    //前端需要的，刚好为page对象的records和total
    //    protected List<T> records;
    //    protected long total;



    /*
     * @Description 根据id更新employee，通用更新操作（锁定用户） 编辑用户
     * @param employee
     * @return R
     **/
    @PutMapping
    public R update(@RequestBody Employee employee,HttpServletRequest request){
        //前端发送请求如何封装到employee？

        long id = Thread.currentThread().getId();
        log.info("线程id为{}",id);

        //前端传过来的id出问题。。
        //js只能保证前Long16位精度保证。。丢失精度。。
        //解决方案：在服务器给页面响应json数据时将Long统一转化为字符串。加入消息转换器。（如何实现呢？）json如何转换java类型。。
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }


    //http://localhost:8080/employee/1664164826019651585
    //回显用户信息（根据id回显）
    /*
     * @Description 根据id来查询员工信息
     * @param id
     * @return R
     **/
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);
        Assert.isNotNull(employee, ResponseEnum.EMPLOYE_NOTFOUND);
        return R.success(employee);
    }

}
