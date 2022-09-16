package com.test.exception;

import com.test.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResult handleBusinessException(BusinessException e) {
        BaseResult r = new BaseResult();
        r.setCode(e.getCode());
        r.setMsg(e.getMsg());

        return r;
    }

    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResult.error();
    }
}
