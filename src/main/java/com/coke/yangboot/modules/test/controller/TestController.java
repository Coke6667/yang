package com.coke.yangboot.modules.test.controller;

import com.coke.yangboot.common.log.SysLog;
import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.common.utils.redis.RedisUtil;
import com.coke.yangboot.modules.test.dto.QueryDto;
import com.coke.yangboot.modules.test.dto.TestDto;
import com.coke.yangboot.modules.test.entity.Boy;
import com.coke.yangboot.modules.test.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试")
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("拦截器拦截测试")
    @RequestMapping(value = "/interceptor",method = RequestMethod.GET)
    public void test(){
        System.out.println("interceptor!!!");
    }

    @SysLog(functionName = "111",methodName = "222")
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ApiOperation("拦截器放行测试")
    public String login(){
        return "fucking good";
    }

    @RequestMapping(value = "/reslut",method = RequestMethod.GET)
    @ApiOperation("返回格式测试")
    public Result result(){
        return Result.success("返回");
    }

    @RequestMapping(value = "/mybatis",method = RequestMethod.GET)
    @ApiOperation("mybatis测试")
    public Result list(){
        return testService.getList();
    }

    @RequestMapping(value = "/pagehelper",method = RequestMethod.POST)
    @ApiOperation("分页测试")
    public Result page(QueryDto dto){
        return testService.getPage(dto);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation("新增")
    public Result add(TestDto dto){
        return testService.add(dto);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ApiOperation("删除")
    public Result delete(TestDto dto){
        return testService.delete(dto);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation("修改")
    public Result update(TestDto dto){
        return testService.update(dto);
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation("查询")
    public Result lists(){
        return testService.list();
    }

    @RequestMapping(value = "/transactional",method = RequestMethod.POST)
    @ApiOperation("事务测试")
    @Transactional
    public Result transactional(TestDto dto){
        return testService.transactional(dto);
    }

    @RequestMapping(value = "/ocr",method = RequestMethod.POST)
    @ApiOperation("OCR测试")
    public Result ocr(byte[] imgByte){
        return testService.ocr(imgByte);
    }

    @RequestMapping(value = "/thread",method = RequestMethod.GET)
    @ApiOperation("多线程")
    public Result thread(){
        return testService.thread();
    }

    @RequestMapping(value = "/output",method = RequestMethod.GET)
    @ApiOperation("导出测试")
    public Result output(){
        return testService.output();
    }

    @RequestMapping(value = "scheduled",method = RequestMethod.GET)
    @ApiOperation("Scheduled测试")
    @Scheduled(cron = "0 0/10 * * * *")
    public void Scheduled(){
        System.out.println("1111111111");
    }

    @RequestMapping(value = "Serializable",method = RequestMethod.GET)
    @ApiOperation("Serializable测试")
    public void SerializableTest() throws IOException, ClassNotFoundException {
        Boy boy1 = new Boy();
        boy1.setGirlFriendCount(10);
        //将对象序列化为文件
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("boy.txt")));
        objectOutputStream.writeObject(boy1);
        //反序列化创建对象
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("boy.txt")));
        Boy boy2 = (Boy) objectInputStream.readObject();
        System.out.println(boy2.getGirlFriendCount());
    }
    @RequestMapping(value = "heartBeat",method = RequestMethod.GET)
    @ApiOperation("redis心跳测试")
    public void testt(String id) {
        redisUtil.set("onlion_"+id,true,15);
    }

    @RequestMapping(value = "online",method = RequestMethod.GET)
    @ApiOperation("通过心跳判断在线状态")
    public void onlion(String id){
        if (redisUtil.get("onlion_"+id)==null){
            log.info(id+"不在线");
        }else {
            log.info(id+"在线");
        }
    }

}
