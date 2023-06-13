package com.coke.yangboot.modules.test.service;

import com.coke.yangboot.common.result.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface MinioService {
    Result upload(MultipartFile file);

    Result preview(String fileName);

    Result remove(String fileName);

    void download(String fileName, HttpServletResponse httpServletResponse);
}
