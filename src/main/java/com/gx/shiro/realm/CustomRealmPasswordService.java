package com.gx.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CustomRealmPasswordService
 * @Description 自定义加密的Realm -- 使用DefaultHashService（默认 SHA-256 算法）
 * @Authtor guoxiang
 * @Date 2020/5/7 9:47
 **/
public class CustomRealmPasswordService extends AuthorizingRealm {

    // 为了方便，直接注入一个 passwordService 来加密密码，实际使用时需要在 Service 层使用 passwordService 加密密码并存到数据库。
    private PasswordService passwordService;
    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 从 token 中获取用户身份信息
        String username = (String) token.getPrincipal();
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
        // passwordService.encryptPassword(dbPassword) 使用加密后的密码进行认证
        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(username, passwordService.encryptPassword(dbPassword) , getName());
        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 空实现
        return new SimpleAuthorizationInfo();
    }

}
