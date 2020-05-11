package com.gx.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @ClassName CustomRealmImplementRealm
 * @Description 自定义realm第一个 - 配合多个Realm测试
 * @Authtor guoxiang
 * @Date 2020/5/11 10:28
 **/
public class MyRealm2 implements Realm {
    /**
     * 返回一个唯一的Realm名字
     * @return
     */
    @Override
    public String getName() {
        return "MyRealm2";
    }

    /**
     * 判断此Realm是否支持某些Token
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 根据Token获取认证信息
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String phoneNum = (String)token.getPrincipal();  //得到手机号
        String password = new String((char[])token.getCredentials()); //得到密码
        if(!"17607490517".equals(phoneNum)) {
            throw new UnknownAccountException(); //手机号错误
        }
        if(!"123456".equals(password)) {
            throw new IncorrectCredentialsException(); //密码错误
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(phoneNum, password, getName());
    }
}
