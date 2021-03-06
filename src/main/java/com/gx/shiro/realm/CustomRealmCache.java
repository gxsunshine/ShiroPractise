package com.gx.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
  *@ClassName CustomRealmCache
  *@Description 自定义Realm -- 有cache机制的
  *@Authtor guoxiang
  *@Date 2020/5/12 14:59
 **/
public class CustomRealmCache extends AuthorizingRealm {

    /**
     * 认证
     * 如果没有可认证缓存，这个方法每次登陆都会被调用。
     * 如果开了认证缓存，这个方法只有认证缓存中，没有数据才会被调用
     * 实际的认证，login方法是在DefaultSecurityManager中login()中实现的，该实现会获取认证信息，进行对比。
     * 如果缓存中没有该认证信息，就会来这个方法中，获取认证信息。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("第一次登陆，已被缓存记住");
        // 从 token 中获取用户身份信息
        String username = (String) token.getPrincipal();
        System.out.println("username:"+ username);
        // 通过 username 从数据库中查询账号和密码信息
        // 模拟使用静态数据
        String dbUsername = username;
        String dbPassword = "123456";
        // 如果查询不到则返回 null
        if(dbUsername.isEmpty()){//这里模拟查询不到
            return null;
        }

        //返回认证信息由父类 AuthenticatingRealm 进行认证
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, dbPassword, getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 授权
     * 调用时机：
     *   1、判断方法：subject.hasRole(“admin”) 或 subject.isPermitted(“admin”)
     *   2、注解方式: @RequiresRoles("admin")
     *   3、JSP的Shiro标签: [@shiro.hasPermission name = "admin"][/@shiro.hasPermission]
     * 注意：doGetAuthorizationInfo每次访问带有被shiro权限管理的连接，都会被调用，多次授权
     *
     * 授权流程：
     * 1、首先调用 Subject.isPermitted/hasRole*接口，其会委托给 SecurityManager，而 SecurityManager 接着会委托给 Authorizer；
     * 2、Authorizer 是真正的授权者，如果我们调用如 isPermitted(“user:view”)，其首先会通过 PermissionResolver 把字符串转换成相应的 Permission 实例；
     * 3、在进行授权之前，其会调用相应的 Realm 获取 Subject 相应的角色/权限用于匹配传入的角色/权限；（重要）
     * 4、Authorizer 会判断 Realm 的角色/权限是否和传入的匹配，如果有多个 Realm，会委托给 ModularRealmAuthorizer 进行循环判断，如果匹配如 isPermitted/hasRole* 会返回 true，否则返回 false 表示授权失败。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("登陆后，第一次授权，已被缓存");

        // 获取身份信息
        String username = (String) principals.getPrimaryPrincipal();
        // 将权限信息封闭为AuthorizationInfo
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 根据身份信息从数据库中查询权限数据
        // 这里使用静态数据模拟
        List<String> permissions = new ArrayList<String>();
        if(username.equals("gx")){
            permissions.add("user:*");
            permissions.add("department:*");
            // 模拟数据，添加 manager 角色
            simpleAuthorizationInfo.addRole("manager");
        }else{
            permissions.add("user:query");
            simpleAuthorizationInfo.addRole("customer");
        }

        for(String permission:permissions){
            simpleAuthorizationInfo.addStringPermission(permission);
        }

        return simpleAuthorizationInfo;
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }
}
