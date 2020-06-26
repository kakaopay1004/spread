package com.kakaopay.spread.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class RestControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ErrorResponseVO entityNotFoundException(Exception ex) {
        log.error(ex.getMessage());

        return ErrorResponseVO.builder()
                .error("EntityNotFoundException")
                .message("데이터가 존재하지 않습니다.")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

}
