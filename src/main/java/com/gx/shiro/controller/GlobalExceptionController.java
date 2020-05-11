package com.gx.shiro.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalExceptionController
 * @Description 全局异常处理
 * @Authtor guoxiang
 * @Date 2020/5/8 10:34
 **/
// @RestControllerAdvice = @responseBody + @ControllerAdvice
// @ControllerAdvice 是一个增强版的Controller,一般用于1，全局异常处理；2，全局数据绑定；3，全局数据预处理；
// 参考：https://www.cnblogs.com/lenve/p/10748453.html
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception ex) {
        return "无权限";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception ex) {
        return "权限认证失败";
    }
}
