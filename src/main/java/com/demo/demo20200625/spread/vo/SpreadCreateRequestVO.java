package com.demo.demo20200625.spread.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpreadCreateRequestVO {

    private int money;
    private int count;

    @Builder
    public SpreadCreateRequestVO(int money,  int count) {
        this.money = money;
        this.count = count;
    }

}
