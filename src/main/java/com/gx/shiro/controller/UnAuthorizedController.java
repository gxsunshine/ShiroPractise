package com.gx.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UnAuthorizedController
 * @Description TODO
 * @Authtor guoxiang
 * @Date 2020/5/8 9:40
 **/
@RestController
public class UnAuthorizedController {

    @RequestMapping("/unAuthorized")
    public String unAuthorized(){
        return "没有权限";
    }
}
