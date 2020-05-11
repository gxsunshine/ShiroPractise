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
 * @ClassName CustomRealm
 * @Description 自定义Realm
 * @Authtor guoxiang
 * @Date 2020/5/7 9:47
 **/
public class CustomRealm extends AuthorizingRealm {

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 从 token 中获取用户身份信息
        String username = (String) token.getPrincipal();
//        String password = (String) token.getCredentials();
        System.out.println("username:"+ username);
//        System.out.println("password:"+ password);
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
     * 当前用户登录通过认证后。再次访问其他连接，就会调用这个方法给这个用户进行授权。
     * 注意：doGetAuthorizationInfo每次访问带有被shiro权限管理的连接，都会被调用，多次授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
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

}
