package com.coke.yangboot.modules.test.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.common.utils.minio.MinioUtil;
import com.coke.yangboot.modules.test.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service
public class MinioServiceImpl implements MinioService {
    @Autowired
    public MinioUtil minioUtil;

    @Override
    public Result upload(MultipartFile file) {
        String msg = minioUtil.upload(file);
        if (StringUtils.isBlank(msg)){
            return Result.fail();
        }
        return Result.success(msg);
    }

    @Override
    public Result preview(String fileName) {
        String msg = minioUtil.preview(fileName);
        if (StringUtils.isBlank(msg)){
            return Result.fail();
        }
        return Result.success(msg);
    }

    @Override
    public Result remove(String fileName) {
        boolean msg = minioUtil.remove(fileName);
        if (msg){
            return Result.success();
        }
        return Result.fail();
    }

    @Override
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.download(fileName,response);
    }
}
