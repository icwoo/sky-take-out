package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description OssConfiguration
 * @Author Lisheng Li
 * @Date 2025-08-08
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean//意思好像是：没有这种bean的时候再去创建
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        //下面的是手动new AliOssProperties，跟上面的自动注入意思一样，结果相同
        //AliOssProperties aliOssProperties = new AliOssProperties();

        log.info("开始创建阿里云文件上传工具类对象，{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
