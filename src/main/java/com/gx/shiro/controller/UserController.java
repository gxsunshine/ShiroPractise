package com.gx.shiro.controller;

import com.gx.shiro.realm.CustomRealmCache;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.RealmSecurityManager;
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

    @RequiresPermissions("user:updatePwd")
    @RequestMapping("/updatePwd")
    protected String updatePwd(){
        // 修改密码后，应该清除认证的缓存，不然还是会把登陆传入的密码跟缓存中的旧密码进行对比，不会从realm中读取新的密码
        RealmSecurityManager securityManager =
                (RealmSecurityManager) SecurityUtils.getSecurityManager();
        CustomRealmCache userRealm = (CustomRealmCache) securityManager.getRealms().iterator().next();
        // 清空认证缓存信息
        userRealm.clearCachedAuthenticationInfo(SecurityUtils.getSubject().getPrincipals());
        System.out.println("认证信息缓存被清理");
        return "修改密码成功";
    }

    @RequiresPermissions("user:updatePermissions")
    @RequestMapping("/updatePermission")
    protected String updatePermission(){
        // 修改权限后，应该清除授权的缓存，不然还是会用缓存中的权限进行权限验证
        RealmSecurityManager securityManager =
                (RealmSecurityManager) SecurityUtils.getSecurityManager();
        CustomRealmCache userRealm = (CustomRealmCache) securityManager.getRealms().iterator().next();
        // 清空授权缓存信息
        userRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
        System.out.println("授权信息缓存被清理");
        return "修改权限成功";
    }
}
