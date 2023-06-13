package com.coke.yangboot.modules.test.service;

import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.modules.test.dto.QueryDto;
import com.coke.yangboot.modules.test.dto.TestDto;
import net.sourceforge.tess4j.TesseractException;

public interface TestService {
    Result getList();

    Result getPage(QueryDto dto);

    Result add(TestDto dto);

    Result delete(TestDto dto);

    Result update(TestDto dto);

    Result list();

    Result transactional(TestDto dto);

    Result ocr(byte[] imgByte);

    Result thread();

    Result output();
}
