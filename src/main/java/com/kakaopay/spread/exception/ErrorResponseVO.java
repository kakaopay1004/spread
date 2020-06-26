package com.kakaopay.spread.exception;

import com.kakaopay.spread.code.SpreadCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class ErrorResponseVO {

    @Builder.Default
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(SpreadCode.DATETIME_FORMAT));

    private int status;

    private String error;

    private String message;

    @Builder.Default
    private String path = "";

}
