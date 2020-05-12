package com.gx.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
  *@ClassName logoutController
  *@Description 用户注销
  *@Authtor guoxiang
  *@Date 2020/5/12 15:32
 **/
@RestController
public class logoutController {

    @RequestMapping("/logout")
    public String login() {
        // 从SecurityUtils中获取当前用户（Subject）
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "注销成功";
    }

}
