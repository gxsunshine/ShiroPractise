package com.gx.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName loginController
 * @Description shiro的登陆验证
 * @Authtor guoxiang
 * @Date 2020/5/6 19:43
 **/
@RestController
public class loginController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }


    @RequestMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        // 从SecurityUtils中获取当前用户（Subject）
        Subject currentUser = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            return "未知账户";
        } catch (IncorrectCredentialsException ice) {
            return "密码不正确";
        } catch (LockedAccountException lae) {
            return "账户已锁定";
        } catch (ExcessiveAttemptsException eae) {
            return "用户名或密码错误次数过多";
        } catch (AuthenticationException ae) {
            return "用户名或密码不正确！";
        }
        if (currentUser.isAuthenticated()) {
            return "登录成功";
        } else {
            token.clear();
            return "登录失败";
        }
    }

}
