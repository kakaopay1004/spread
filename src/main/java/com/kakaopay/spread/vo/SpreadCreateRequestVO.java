package com.kakaopay.spread.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel
public class SpreadCreateRequestVO {

    @ApiModelProperty(value = "뿌리기 금액", example = "10000", required = true)
    private int money;

    @ApiModelProperty(value = "뿌리기 인원", example = "3", required = true)
    private int count;

    @Builder
    public SpreadCreateRequestVO(int money,  int count) {
        this.money = money;
        this.count = count;
    }

}
