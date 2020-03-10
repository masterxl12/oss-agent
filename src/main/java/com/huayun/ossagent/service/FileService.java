package com.huayun.ossagent.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.huayun.ossagent.util.OssClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileService {
    @Autowired
    private OssClient ossClient;

    public List<OSSObjectSummary> getFiles(String bucketName){
        OSSClient client = ossClient.getOssClient();
        // 获取指定bucket下的所有Object信息
        ObjectListing objectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> lists = objectListing.getObjectSummaries();
        return lists;
    }

//    public static void main(String[] args) {
//        FileService fs = new FileService();
//        fs.getFiles("file-service");
//    }
}
