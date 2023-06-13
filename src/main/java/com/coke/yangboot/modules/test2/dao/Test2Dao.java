package com.coke.yangboot.modules.test2.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coke.yangboot.modules.test2.dto.Test2Dto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Test2Dao extends BaseMapper<Test2Dto> {

    List<Test2Dto> getUserList();

    void saveUser(Test2Dto test2Dto);

}
