package com.likejin.reggie.config;

import com.likejin.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @Author 李柯锦
 * @Date 2023/5/31 14:24
 * @Description
 */

@Slf4j
@Configuration
public class WebMVCConfig extends WebMvcConfigurationSupport {

    /*
     * @Description 设置静态资源映射。。默认springmvc只能访问到static目录
     * @param registry
     * @return void
     **/
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/backend/");

        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/front/");

        log.info("开始进行静态资源映射");
    }

    /*
     * @Description 扩展MVC框架的消息转换器 将controller返回结果转换相应json。将前端传入的json转换为对应的对象。
     * @param converters
     * @return void
     **/
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器。。底层使用jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        super.extendMessageConverters(converters);
        //将消息转换器对象追加到mvc框架的转换器集合中，将优先级设置为0优先使用
        converters.add(0,messageConverter);
    }
}
