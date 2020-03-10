package com.huayun.ossagent.controller;

import com.huayun.ossagent.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/getfiles", method = RequestMethod.GET)
    public List getFiles(@RequestParam String bucketName) {
         return fileService.getFiles(bucketName);
    }
}
