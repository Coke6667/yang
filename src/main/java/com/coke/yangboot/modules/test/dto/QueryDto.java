package com.coke.yangboot.modules.test.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询参数")
public class QueryDto {
    @ApiModelProperty(value = "大小")
    private int pageSize;
    @ApiModelProperty(value = "页码")
    private int pageNo;
}
