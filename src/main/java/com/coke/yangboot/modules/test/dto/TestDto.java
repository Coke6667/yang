package com.coke.yangboot.modules.test.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.url.UrlImageConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.net.URL;

@Data
@ApiModel("测试实体")
@TableName(value = "user_info")
@ContentRowHeight(100)
@HeadRowHeight(20)
public class TestDto {
    @TableId(type = IdType.ASSIGN_ID)
    @ExcelProperty("Id")
    @ApiModelProperty(value = "id")
    private String id;

    @ExcelProperty("用户名")
    @ApiModelProperty("用户名")
    private String userName;

    @ExcelProperty("年龄")
    @ApiModelProperty("年龄")
    private String age;

    @ExcelProperty(value = "图片",converter = UrlImageConverter.class)
    @ColumnWidth(20)
    @ApiModelProperty("图片")
    private URL picture;

    @ExcelIgnore
    @ApiModelProperty("图片Dto")
    private String url;
}
