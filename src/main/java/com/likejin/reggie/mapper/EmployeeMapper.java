package com.likejin.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.likejin.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 李柯锦
 * @Date 2023/5/31 14:43
 * @Description
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
