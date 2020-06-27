package com.kakaopay.spread.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel
public class ReceiveUserResponseVO {

    @ApiModelProperty(value = "사용자 아이디")
    private Long userId;

    @ApiModelProperty(value = "받은 금액")
    private int money;

}
