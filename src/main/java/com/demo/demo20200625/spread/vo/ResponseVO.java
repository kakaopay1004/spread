package com.demo.demo20200625.spread.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseVO {

    private String code;

    @Builder
    public ResponseVO(String code) {
        this.code = code;
    }
}
