package com.kakaopay.spread.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class SpreadCreateResponseVO {

    @ApiModelProperty(value = "뿌리기 토큰", example = "E3y", required = true)
    private String token;

}
