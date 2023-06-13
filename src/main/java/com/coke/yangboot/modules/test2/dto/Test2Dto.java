package com.coke.yangboot.modules.test2.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("测试实体")
@TableName(value = "user_info")
public class Test2Dto {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id",hidden = true)
    private String id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("年龄")
    private String age;
}
