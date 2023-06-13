package com.coke.yangboot.modules.stream;

import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.modules.test.dao.TestDao;
import com.coke.yangboot.modules.test.dto.TestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stream")
@Api("steram")
public class StreamController {
    @Autowired
    private TestDao testDao;
    @GetMapping("/getIds")
    @ApiOperation("获取ids逗号隔开")
    public Result getIds(){
        String ids= testDao.getList().stream().filter(dto -> dto.getUserName().equals("张三")).map(TestDto::getId).collect(Collectors.joining(","));
        System.out.println(ids);
        return Result.success(ids);
    }
}

