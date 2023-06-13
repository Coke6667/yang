package com.coke.yangboot.modules.test.service.impl;

import com.alibaba.excel.EasyExcel;
import com.coke.yangboot.common.result.Result;
import com.coke.yangboot.modules.test.dao.TestDao;
import com.coke.yangboot.modules.test.dto.QueryDto;
import com.coke.yangboot.modules.test.dto.TestDto;
import com.coke.yangboot.modules.test.service.TestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TestServiceImpl implements TestService {
    //线程池
    public static ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(20);
    @Autowired
    private TestDao testDao;
    @Override
    public Result getList() {
        List<TestDto> list = testDao.getList();
        return Result.success(list);
    }

    @Override
    public Result getPage(QueryDto dto) {
        PageHelper.startPage(dto.getPageNo(),dto.getPageSize());
        List<TestDto> list = testDao.getList();
        PageInfo<TestDto> pageInfo = new PageInfo<>(list);
        return Result.success(pageInfo);
    }

    @Override
    public Result add(TestDto dto) {
        testDao.insert(dto);
        return Result.success();
    }

    @Override
    public Result delete(TestDto dto) {
        testDao.deleteById(dto);
        return Result.success();
    }

    @Override
    public Result update(TestDto dto) {
        testDao.updateById(dto);
        return Result.success();
    }

    @Override
    public Result list() {
        List<TestDto> list = testDao.selectList(null);
        return Result.success(list);
    }

    @Override
    public Result transactional(TestDto dto){
        testDao.insert(dto);
        int[] i = {1,2};
        int j = i[2];
        return Result.success();
    }

    @Override
    public Result ocr(byte[] imgByte){
        try{
            File file = new File("F:\\1.jpg");
            //创建Tesseract对象
            ITesseract tesseract = new Tesseract();
            //设置字体库路径
            tesseract.setDatapath("F:\\tessdata");
            //中文识别
            tesseract.setLanguage("chi_sim");
            //执行ocr识别
            String result = tesseract.doOCR(file);
            //替换回车和tal键  使结果为一行
            result = result.replaceAll("\\r|\\n","-").replaceAll(" ","");
            System.out.println("识别的结果为："+result);
            return Result.success(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result thread() {
        System.out.println(Thread.currentThread().getName());
        new Thread() {
            @Override
            public void run() {
                System.out.println(1111 + Thread.currentThread().getName());
            }
        }.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(2222 + Thread.currentThread().getName());
            }
        }).start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(3333 + Thread.currentThread().getName());
            }
        };
        new Thread(r).start();
        new Thread(() -> {
            System.out.println(4444 + Thread.currentThread().getName());
        }).start();

        //使用线程池
        newFixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(5555+ Thread.currentThread().getName());
            }
        });

        return null;
    }

    @Override
    public Result output() {
        List<TestDto> list = testDao.getList();
        for (TestDto testDto : list) {
            try {
                testDto.setPicture(new URL(testDto.getUrl()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        EasyExcel.write("F:/TT/aaa.xlsx", TestDto.class).sheet("模板").doWrite(list);
        return null;
    }

}


