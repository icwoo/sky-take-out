package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Description CommonController
 * @Author Lisheng Li
 * @Date 2025-08-07
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "文件上传接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();

            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            //构造新文件名称
            String objectName = UUID.randomUUID() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败：{}",e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);//就是个常量，不这么写的话直接返回字符串也行
    }
}
