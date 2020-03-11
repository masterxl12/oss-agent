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
    public java.lang.String deleteBucket(@RequestParam String bucketName) {
        return fileService.deleteBucket(bucketName);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam String bucketName, @RequestParam String fileName, @RequestParam MultipartFile file) {
        return fileService.upload(bucketName, fileName, file);
    }

}
