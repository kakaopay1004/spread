package com.demo.demo20200625.spread.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpreadCreateResponseVO {

    private String code;
    private String token;

    @Builder
    public SpreadCreateResponseVO(String code, String token) {
        this.code = code;
        this.token = token;
    }
}
