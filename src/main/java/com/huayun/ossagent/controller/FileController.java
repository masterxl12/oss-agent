package com.huayun.ossagent.controller;

import com.huayun.ossagent.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/oss")
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "createBucket", method = RequestMethod.POST)
    public String createBucket(@RequestParam String bucketName) {
        return fileService.createBucket(bucketName);
    }

    @RequestMapping(value = "/getfiles", method = RequestMethod.GET)
    public List getFiles(@RequestParam String bucketName) {
        return fileService.getFiles(bucketName);
    }

    @RequestMapping(value = "/allbuckets", method = RequestMethod.GET)
    public List getBuckets() {
        return fileService.getBuckets();
    }

    @RequestMapping(value = "/deleteBucket", method = RequestMethod.POST)
    public String deleteBucket(@RequestParam String bucketName) {
        return fileService.deleteBucket(bucketName);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam String bucketName, @RequestParam String filePath, @RequestParam MultipartFile file) {
        return fileService.upload(bucketName, filePath, file);
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public String uploads(@RequestParam String bucketName, @RequestParam String filePath, @RequestParam MultipartFile[] files) {
        return fileService.uploads(bucketName, filePath, files);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam String bucketName, @RequestParam String objectName) {
        return fileService.delete(bucketName, objectName);

    }

    @RequestMapping(value = "/deletefiles", method = RequestMethod.POST)
    public String deletefiles(@RequestParam String bucketName, @RequestParam String[] objectName) {
        return fileService.deleteFiles(bucketName, objectName);

    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String download(@RequestParam String bucketName, @RequestParam String objectName, @RequestParam String localPath) {
        return fileService.download(bucketName, objectName, localPath);
    }


}
