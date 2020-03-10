package com.huayun.ossagent.controller;

import com.aliyun.oss.OSSClient;
import com.huayun.ossagent.util.OssClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    @Autowired
    private OssClient ossClient;

    @RequestMapping(value = "/getClient",method = RequestMethod.GET)
    public void getClient(){
        OSSClient ossclient = ossClient.getOssClient();
        System.out.println(ossclient.getEndpoint());
    }
}
