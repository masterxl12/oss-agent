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
import java.util.ArrayList;
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
        client.shutdown();

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
        client.shutdown();

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
        client.shutdown();
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
        client.shutdown();
        return deleteBucketTips;
    }


    /**
     * 单文件上传
     *
     * @param bucketName
     * @param remotePath
     * @param file
     * @return
     */
    public String upload(String bucketName, String remotePath, MultipartFile file) {
        String uploadTips = "";
        OSSClient client = ossClient.getOssClient();
        String fileName = file.getOriginalFilename();
        String uploadPath = remotePath + "/" + fileName;
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
            uploadTips = "文件 " + file + " 上传失败！";
        }
        client.shutdown();
        return uploadTips;
    }

    public String uploads(String bucketName, String remotePath, MultipartFile[] files) {
        String uploadTips = "";
        OSSClient client = ossClient.getOssClient();
        boolean exists = client.doesBucketExist(bucketName);
        if (exists) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getOriginalFilename();
                // 先判断是否直接存储在桶下
                InputStream inputStream = null;
                try {
                    inputStream = files[i].getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 如果是直接上传到存储桶下，传入filePath = "/"即可
                if (remotePath.equals("/")) {
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);
                    client.putObject(putObjectRequest);
                    uploadTips += fileName + ",";
                } else { // 如果是上传到存储桶指定的子集目录下，形如filePath = "xxx/yyy"格式即可
                    String uploadPath = remotePath + "/" + fileName;
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uploadPath, inputStream);
                    client.putObject(putObjectRequest);
                }
            }
            uploadTips = "OK! " + uploadTips + " 上传成功！";
        } else {
            uploadTips = "文件上传到 " + remotePath + " 失败！";
        }
        client.shutdown();
        return uploadTips;
    }

    /**
     * 删除单个文件
     *
     * @param bucketName 存储桶名
     * @param objectName 指定存储桶下的文件对象
     * @return
     */
    public String delete(String bucketName, String objectName) {
        String deleteTips = "";
        OSSClient client = ossClient.getOssClient();
        boolean bucketExist = client.doesBucketExist(bucketName);
        // 判断存储桶和文件对象是否存在
        boolean objectExist = client.doesObjectExist(bucketName, objectName);
        if (bucketExist && objectExist) {
            client.deleteObject(bucketName, objectName);
            deleteTips = objectName + "，文件删除成功！";
        } else {
            deleteTips = objectName + "，文件删除失败！，该对象不存在";
        }
        client.shutdown();
        return deleteTips;
    }

    /**
     * 批量删除
     *
     * @param bucketName
     * @param files
     * @return
     */
    public String deleteFiles(String bucketName, String[] files) {
        String deleteTips = "";
        OSSClient client = ossClient.getOssClient();
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            keys.add(files[i]);
            deleteTips += "文件对象: " + files[i] + "\n";
        }
        DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        deleteTips += "批量删除成功!";
        client.shutdown();
        return deleteTips;
    }

    /**
     * 删除指定存储桶下的文件夹
     * oss默认没有文件目录的概念，根据官网说明，只要把当前目录下的所有文件删除后，即能删除该目录
     * 实现步骤：
     * 0。oss在我们上传文件(api调用)/客户端/新建文件夹时,默认都会给文件对象绑定key(其实就是相对于
     *          bucketName的相对路径名)，删除文件也要用到这个key，
     *          ============================================================
     *          官方删除文件对象的api:
     *              client.deleteObject(String bucketName,String key)
     *          因此: key(文件路径名) ====实现删除单个、多个、甚至是文件目录的关键===
     *          ============================================================
     * 1。将指定删除的目录名设为遍历查找的prefix(文件前缀)
     * 2。根据prefix列出所有的key，添加到String[]
     *    这里封装了一个getFilesKey(bucketName,fileName)的成员方法，方便获取keys
     * 3。调用封装的deleFiles成员方法
     *
     * @param bucketName
     * @param dirName
     * @return
     */
    public String deleteDir(String bucketName, String dirName) {
        String deleteTips = "";
        OSSClient client = ossClient.getOssClient();
        // 判断存储桶和文件夹对象是否存在
        boolean bucketExist = client.doesBucketExist(bucketName);
        ArrayList<String> lists = getFilesKey(bucketName, dirName);
        String[] keysArr = new String[lists.size()];
        lists.toArray(keysArr);
        deleteTips = deleteFiles(bucketName, keysArr);
        for (String item : keysArr) {
            System.out.println(item);
        }
        return deleteTips;
    }

    /**
     * 根据文件目录名获取当前目录下的所有keys
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    public ArrayList<String> getFilesKey(String bucketName, String fileName) {
        OSSClient client = ossClient.getOssClient();
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
//        //Delimiter 设置为 “/” 时，罗列该文件夹下的文件
//        listObjectsRequest.setDelimiter("/");
        //Prefix 设为某个文件夹名，罗列以此 Prefix 开头的文件
        listObjectsRequest.setPrefix(fileName + "/");
        ObjectListing listing = client.listObjects(listObjectsRequest);
        ArrayList<String> list = new ArrayList<String>();
        // 遍历所有Object:目录下的文件
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            String key = objectSummary.getKey();
            list.add(key);
        }
        return list;
    }

    /**
     * 文件下载功能
     *
     * @param bucketName   存储桶名
     * @param remoteObject oss服务的文件对象名
     * @param localPath    oss服务的文件对象名
     * @return
     */
    public String download(String bucketName, String remoteObject, String localPath) {
        String downloadTips = "";
        OSSClient client = ossClient.getOssClient();
        boolean exist = client.doesBucketExist(bucketName);
        boolean bucketExist = client.doesObjectExist(bucketName, remoteObject);
        if (exist && bucketExist) {
            // 指定文件保存路径
//            if (objectName.contains("/")) {
//                //判断文件目录是否存在，不存在则创建
//                filePath = localPath;
//            } else { // objectName没有"/"这种情况，也即objectName对象直接存储在存储桶一级目录下
//                filePath = localPath + "/" + objectName;
//            }
            String filePath = localPath;
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断保存文件名是否加后缀
            if (remoteObject.contains(".")) {
                //指定文件保存名称
                filePath = filePath + "/" + remoteObject.substring(remoteObject.lastIndexOf("/") + 1);
            }
            //获取OSS文件并保存到本地指定路径中，此文件路径一定要存在，若保存目录不存在则报错，若保存文件名已存在则覆盖本地文件
            client.getObject(new GetObjectRequest(bucketName, remoteObject), new File(filePath));
            downloadTips = "oK，" + remoteObject + "文件下载成功";
        } else {
            downloadTips = "下载文件失败!";
        }
        client.shutdown();
        return downloadTips;
    }

    /**
     * 批量下载功能
     *
     * @param bucketName
     * @param remoteObjects
     * @param localPath
     * @return
     */
    public String downloads(String bucketName, ArrayList<String> remoteObjects, String localPath) {
        String downloadTips = "";
        OSSClient client = ossClient.getOssClient();
        boolean exist = client.doesBucketExist(bucketName);
        if (exist) {
            String filePath = localPath;
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            for (String remoteObject : remoteObjects) {
                //判断保存文件名是否加后缀
                if (remoteObject.contains(".")) {
                    //指定文件保存名称
                    filePath = filePath + "/" + remoteObject.substring(remoteObject.lastIndexOf("/") + 1);
                }
                //获取OSS文件并保存到本地指定路径中，此文件路径一定要存在，若保存目录不存在则报错，若保存文件名已存在则覆盖本地文件
                client.getObject(new GetObjectRequest(bucketName, remoteObject), new File(filePath));
                downloadTips += "oK，" + remoteObject + "文件下载成功" + "\n";
                filePath = filePath.substring(0, filePath.lastIndexOf("/"));
            }
        } else {
            downloadTips = "下载文件失败!";
        }
        client.shutdown();
        return downloadTips;
    }


    public static void main(String[] args) {
        FileService fileService = new FileService();
        String fileName = "abc/efg/fff.doc";
        String filePath1 = fileName + "/" + fileName.substring(0, fileName.lastIndexOf("/"));

        String filePath2 = fileName.substring(fileName.lastIndexOf("/") + 1);
        System.out.println(filePath1);
        System.out.println(filePath2);
        System.out.println(fileName.substring(0, fileName.lastIndexOf("/")));
//        fileService.download("oss-xl", "1.png");

//        String a = "abc, 123,fff";
//        String[] arr = a.split(";");
//        List<String> keys = new ArrayList<String>();
//        for (int i = 0; i < arr.length; i++) {
//            keys.add(arr[i]);
//        }
//        System.out.println(keys);
    }
}
