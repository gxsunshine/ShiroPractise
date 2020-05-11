package com.gx.shiro.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Authtor guoxiang
 * @Date 2020/5/7 19:28
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @RequiresPermissions("user:query")
    @RequestMapping("/query")
    public String query(){
        return "查询用户信息";
    }

    @RequiresPermissions("user:add")
    @RequestMapping("/add")
    public String add(){
        return "添加用户";
    }

    @RequiresRoles("manager")
    @RequestMapping("/delete")
    public String delete(){
        return "删除用户";
    }
}
