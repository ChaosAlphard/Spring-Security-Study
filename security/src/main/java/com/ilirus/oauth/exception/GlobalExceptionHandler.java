package com.ilirus.oauth.exception;

import com.ilirus.oauth.entities.Dto;
import com.ilirus.oauth.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.OK)
    public Dto UserNotFound(UsernameNotFoundException unfe) {
        log.error("找不到用户",unfe);
        return Dto.of(Status.ACCESS_DENIED);
    }
}
