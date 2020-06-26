package com.demo.demo20200625.spread.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {

//    @ExceptionHandler(value = {Exception.class})
//    @ResponseStatus(HttpStatus.OK)
//    public ErrorResponseVO unknownException(Exception ex) {
//        log.error(ex.getMessage(), ex);
//
//        return ErrorResponseVO
//                .builder()
//                .code(999)
//                .build();
//    }

}