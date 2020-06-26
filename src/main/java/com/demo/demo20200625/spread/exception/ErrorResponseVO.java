package com.demo.demo20200625.spread.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
public class ErrorResponseVO {

    @Getter
    private int code;

    public ErrorResponseVO(int code) {
        this.code = code;
    }
}
