package com.coke.yangboot.modules.test.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.common.utils.minio.MinioUtil;
import com.coke.yangboot.modules.test.service.MinioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/minio")
@Api(tags = "minio测试")
@Slf4j
public class MinioController {
    @Autowired
    public MinioService minioService;

    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;
    @ApiOperation(value = "文件上传")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile file){
        return minioService.upload(file);
    }

    @ApiOperation(value = "文件预览")
    @RequestMapping(value = "/preview",method = RequestMethod.GET)
    public Result preview(String fileName){
        return minioService.preview(fileName);
    }

    @ApiOperation(value = "文件删除")
    @RequestMapping(value = "/remove",method = RequestMethod.GET)
    public Result remove(String fileName){
        return minioService.remove(fileName);
    }

    @ApiOperation(value = "文件下载")
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void download(String fileName, HttpServletResponse httpServletResponse){
        minioService.download(fileName,httpServletResponse);
    }
}
