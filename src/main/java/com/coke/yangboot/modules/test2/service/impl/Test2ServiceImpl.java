package com.coke.yangboot.modules.test2.service.impl;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.modules.test.dto.QueryDto;
import com.coke.yangboot.modules.test2.dao.Test2Dao;
import com.coke.yangboot.modules.test2.dto.Test2Dto;
import com.coke.yangboot.modules.test2.service.Test2Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Test2ServiceImpl implements Test2Service {

    @Autowired
    private Test2Dao test2Dao;

    @Override
    public Result getUserList(QueryDto dto){
        PageHelper.startPage(dto.getPageNo(),dto.getPageSize());
        List<Test2Dto> list = test2Dao.getUserList();
        PageInfo<Test2Dto> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }

    @Override
    public void saveUser(Test2Dto test2Dto) {
        test2Dao.saveUser(test2Dto);
    }

    @Override
    public Result plusSelect(Test2Dto test2Dto,QueryDto dto){
        PageHelper.startPage(dto.getPageNo(),dto.getPageSize());
        QueryWrapper<Test2Dto> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(test2Dto.getAge())){
            queryWrapper.ge("age",test2Dto.getAge());
        }
        if(!StringUtils.isNullOrEmpty(test2Dto.getUserName())){
            queryWrapper.eq("user_name",test2Dto.getUserName());
        }
        List<Test2Dto> list = test2Dao.selectList(queryWrapper);
        PageInfo<Test2Dto> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }
}
