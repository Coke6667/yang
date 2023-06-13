package com.coke.yangboot.modules.test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coke.yangboot.modules.test.dto.TestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TestDao extends BaseMapper<TestDto> {
    List<TestDto> getList();
}
