package com.likejin.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author 李柯锦
 * @Date 2023/5/31 14:20
 * @Description 启动项
 */

//lombok的注解 可以打印日志
@Slf4j
@SpringBootApplication
//用于扫描webFilter注解
@ServletComponentScan
//开启事务注解的支持
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class);
        log.info("项目启动成功....");
    }
}
