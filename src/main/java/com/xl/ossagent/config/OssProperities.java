package com.xl.ossagent.config;

import org.springframework.beans.factory.annotation.Value;
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
