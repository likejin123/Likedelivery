package com.likejin.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.likejin.reggie.entity.Employee;
import com.likejin.reggie.mapper.EmployeeMapper;
import com.likejin.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author 李柯锦
 * @Date 2023/5/31 14:45
 * @Description
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

}
