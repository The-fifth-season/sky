package com.sky.controller;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("upload")
    @ApiOperation("上传文件接口")
    public Result<String> upload(@RequestBody MultipartFile file){
        log.info("文件上传{}",file);
        try {
            InputStream is = file.getInputStream();                   //直接用getInputStream转换为输入流
            String OriginalFilename = file.getOriginalFilename();
            String s = null;
            if (OriginalFilename != null) {
                s = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));          //substring(begin),从哪个位置还是截取，截取到末尾
            }                                                                                   //lastIndexOf("."),最后一个.的位置，返回值为int  s的值为文件的后缀.jpg。
            String fileName = UUID.randomUUID().toString() + s;
            String upload = aliOssUtil.upload(is, fileName);     //URL例：https://the-fifth-season.oss-cn-hangzhou.aliyuncs.com/A%7BZGOH5%5DZST%28Q%604G5%24YLOJI.png
            is.close();
            return Result.success(upload);      //返回OSS中存储的路径
        } catch (IOException e) {
            log.error("文件上传失败:",e);
        }
            return Result.error("文件上传失败");
    }
}
