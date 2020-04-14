package com.xl.ossagent.controller;

import com.xl.ossagent.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public String upload(@RequestParam String bucketName, @RequestParam String remotePath, @RequestParam MultipartFile file) {
        return fileService.upload(bucketName, remotePath, file);
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public String uploads(@RequestParam String bucketName, @RequestParam String remotePath, @RequestParam MultipartFile[] files) {
        return fileService.uploads(bucketName, remotePath, files);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam String bucketName, @RequestParam String objectName) {
        return fileService.delete(bucketName, objectName);

    }

    @RequestMapping(value = "/deletefiles", method = RequestMethod.POST)
    public String deletefiles(@RequestParam String bucketName, @RequestParam String[] objects) {
        return fileService.deleteFiles(bucketName, objects);

    }

    @RequestMapping(value = "getKeys", method = RequestMethod.GET)
    public ArrayList getKeys(@RequestParam String bucketName, @RequestParam String fileName) {
        return fileService.getFilesKey(bucketName, fileName);
    }

    @RequestMapping(value = "/deleteDir", method = RequestMethod.POST)
    public String deleteDir(@RequestParam String bucketName, @RequestParam String dirName) {
        return fileService.deleteDir(bucketName, dirName);
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String download(@RequestParam String bucketName, @RequestParam String remoteObject, @RequestParam String localPath) {
        return fileService.download(bucketName, remoteObject, localPath);
    }

    @RequestMapping(value = "/downloads", method = RequestMethod.POST)
    public String downloads(@RequestParam String bucketName, @RequestParam ArrayList<String> remoteObjects, @RequestParam String localPath) {
        return fileService.downloads(bucketName, remoteObjects, localPath);
    }
}
