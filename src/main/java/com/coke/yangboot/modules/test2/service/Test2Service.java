package com.coke.yangboot.modules.test2.service;

import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.modules.test.dto.QueryDto;
import com.coke.yangboot.modules.test2.dto.Test2Dto;

public interface Test2Service {

    Result getUserList(QueryDto dto);

    void saveUser(Test2Dto test2Dto);

    Result plusSelect(Test2Dto test2Dto,QueryDto dto);
}
