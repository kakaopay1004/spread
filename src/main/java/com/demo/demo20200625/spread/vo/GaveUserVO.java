package com.demo.demo20200625.spread.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
public class GaveUserVO {

    private Long userId;

    @Getter
    private int money;

}
