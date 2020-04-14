package com.xl.ossagent.util;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OssClient {
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

    public OSSClient getOssClient(){
        return new OSSClient(endpoint,accessKeyId,accessKeySecret);
    }
}
