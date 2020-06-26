package com.kakaopay.spread.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GaveUserVO {

    private Long userId;
    private int money;

}
