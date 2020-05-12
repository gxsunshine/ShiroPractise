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
import org.apache.shiro.util.ByteSource;

/**
  *@ClassName CustomRealmHashedCredentialsMatcher
  *@Description 自定义加密的Realm -- 使用CustomRealmHashedCredentialsMatcher
  *@Authtor guoxiang
  *@Date 2020/5/12 10:30
 **/
public class CustomRealmHashedCredentialsMatcher extends AuthorizingRealm {

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString(); //获取登陆的用户名
        // 通过 username 从数据库中查询账号和密码信息和salt
        // 模拟使用静态数据
        String dbUsername = username;
        String dbPassword = "61af7f4381dee50506b11b5e61110350"; // 加密后的密码
        String salt = "212a8f2473e742dd437f85cccd11f524"; // 数据库保存的salt
        // 如果查询不到则返回 null
        if(dbUsername.isEmpty()){
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(username, dbPassword, getName());
        // 通过 SimpleAuthenticationInfo 的 credentialsSalt 设置盐，HashedCredentialsMatcher 会自动识别这个盐。
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username+salt)); //盐是用户名+随机数
        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 空实现
        return new SimpleAuthorizationInfo();
    }

}
