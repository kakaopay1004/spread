package com.demo.demo20200625.spread.exception;

import lombok.Getter;

public class SpeardSearchException extends RuntimeException {

    @Getter
    private int code;

    public SpeardSearchException(int code) {
        this.code = code;
    }

}
