package com.kakaopay.spread.interceptor;

import com.kakaopay.spread.code.SpreadCode;
import com.kakaopay.spread.exception.HttpUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class HttpHeaderHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (StringUtils.isBlank(request.getHeader(SpreadCode.X_ROOM_ID))) {
            log.warn("header X-ROOM-ID empty");
            throw new HttpUnauthorizedException();
        }

        if (StringUtils.isBlank(request.getHeader(SpreadCode.X_USER_ID))) {
            log.warn("header X-USER-ID empty");
            throw new HttpUnauthorizedException();
        }

        return true;
    }

}