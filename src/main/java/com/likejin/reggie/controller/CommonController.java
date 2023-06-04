package com.likejin.reggie.controller;

import com.likejin.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * @Author 李柯锦
 * @Date 2023/6/2 19:19
 * @Description 文件上传和下载处理
 */

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    /*
     * @Description 文件上传
     * @param file 参数名必须和前端的
     * @return R
     **/
    @PostMapping("/upload")
    public R upload(MultipartFile file){


        //原始文件名（获取后缀 .jpg .png）
        String originalFilename = file.getOriginalFilename();
        //使用uuid重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString();

        //拼接新文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName = fileName + suffix;

        //如果获得basePath目录不存在则创建
        File dir = new File(basePath);
        if(!dir.exists()){
            //目录不创造创建
            dir.mkdirs();
        }
        //file是临时文件，当请求结束后，临时文件会被删除
        //转存到指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        }catch(Exception e){
            e.printStackTrace();
        }
        //页面需要文件名
        //新增菜品 页面需要保存文件的地址
        return R.success(fileName);
    }

    /*
     * @Description 文件下载
     * @param name
     * @param response
     * @return void
     **/
    @GetMapping("/download")
    public void download(@RequestParam("name") String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将文件写会到浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
