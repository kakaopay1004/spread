package com.kakaopay.spread.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@ApiModel
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpreadSearchResponseVO {

    @ApiModelProperty(value = "시작 일시", example = "E3y", required = true)
    private String spreadDate;

    @ApiModelProperty(value = "전체 금액", example = "E3y", required = true)
    private int money;

    @ApiModelProperty(value = "완료 금액", example = "E3y", required = true)
    private int giveMoney;

    @ApiModelProperty(value = "받은 사용자 정보")
    private List<ReceiveUserResponseVO> receivedUsers;

}
