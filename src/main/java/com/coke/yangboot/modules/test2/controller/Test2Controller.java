package com.coke.yangboot.modules.test2.controller;

import cn.hutool.core.util.IdUtil;
import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.modules.test.dto.QueryDto;
import com.coke.yangboot.modules.test2.dto.Test2Dto;
import com.coke.yangboot.modules.test2.service.Test2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("test")
@Api(tags="测试2")
public class Test2Controller {
    @Autowired
    private Test2Service test2Service;

    @RequestMapping(value = "/userList",method = RequestMethod.GET)
    @ApiOperation("用户表信息")
    public Result log(QueryDto dto){
        return test2Service.getUserList(dto);
    }

    @RequestMapping(value = "/userSave",method = RequestMethod.POST)
    @ApiOperation("添加用户")
    public String saveUser(Test2Dto test2Dto){
        test2Dto.setId(IdUtil.simpleUUID());
        test2Service.saveUser(test2Dto);
        return "保存成功！";
    }

    @RequestMapping(value="plusSelect",method = RequestMethod.POST)
    @ApiOperation("plus查询测试")
    public Result plusSelect(Test2Dto test2Dto,QueryDto dto){
        return test2Service.plusSelect(test2Dto,dto);
    }
}
