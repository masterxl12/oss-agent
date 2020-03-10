package com.huayun.ossagent.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@PropertySource(value = "classpath:application.properties")
//@ConfigurationProperties(prefix = "aliyun")
//@EnableConfigurationProperties(OssProperities.class)

@Component
//@ConfigurationProperties(prefix = "oss")
public class OssProperities {
    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${oss.filePath}")
    private String filePath;


//    @Bean
//    public OSS oSSClient() {
//        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
//    }
}
