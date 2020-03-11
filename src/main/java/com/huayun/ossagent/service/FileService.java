package com.huayun.ossagent.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.huayun.ossagent.util.OssClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class FileService {
    @Autowired
    private OssClient ossClient;

    public String createBucket(String bucketName) {
        String createBucketTips = "";
        OSSClient client = ossClient.getOssClient();
        boolean exists = client.doesBucketExist(bucketName);
        if (exists) {
            createBucketTips = "sorry," + bucketName + "存储桶已经存在，请重新输入";
        } else {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            client.createBucket(createBucketRequest);
            createBucketTips = "ok," + bucketName + " 存储桶创建成功！";
        }
        return createBucketTips;
    }


    /**
     * 列出该accessKeyId下所有的存储桶
     *
     * @return
     */
    public List<Bucket> getBuckets() {
        // 列举存储空间。
        OSSClient client = ossClient.getOssClient();

        List<Bucket> buckets = client.listBuckets();
        return buckets;
    }

    /**
     * 列出指定存储桶下的所有文件
     *
     * @param bucketName
     * @return
     */
    public List<OSSObjectSummary> getFiles(String bucketName) {
        OSSClient client = ossClient.getOssClient();
        // 获取指定bucket下的所有Object信息
        ObjectListing objectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> lists = objectListing.getObjectSummaries();
        return lists;
    }

    /**
     * 删除指定的存储桶
     *
     * @param bucketName
     * @return
     */
    public String deleteBucket(String bucketName) {
        String deleteBucketTips = "";
        OSSClient client = ossClient.getOssClient();
        boolean exists = client.doesBucketExist(bucketName);
        if (!exists) {
            deleteBucketTips = "sorry," + bucketName + "不存在！";
        } else {
            client.deleteBucket(bucketName);
            deleteBucketTips = "ok," + bucketName + "删除成功！";
        }
        return deleteBucketTips;
    }


    /**
     * 单文件上传
     *
     * @param bucketName
     * @param basePath
     * @param file
     * @return
     */
    public String upload(String bucketName, String basePath, MultipartFile file) {
        String uploadTips = "";
        OSSClient client = ossClient.getOssClient();
        String fileName = file.getOriginalFilename();
        String uploadPath =  basePath + "/" + fileName;
        boolean exists = client.doesBucketExist(bucketName);
        if (exists) {
            // 创建PutObjectRequest对象。
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uploadPath, inputStream);
                // 上传文件。
                client.putObject(putObjectRequest);
                uploadTips = "文件 " + bucketName + " 上传成功！";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            uploadTips = "文件 " + bucketName + " 上传失败！";
        }
        return uploadTips;
    }

//    public static void main(String[] args) {
//        FileService fs = new FileService();
//        fs.getFiles("file-service");
//    }
}
