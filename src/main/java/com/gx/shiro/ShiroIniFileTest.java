package com.gx.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @ClassName ShiroIniFileTest
 * @Description Shiro测试 - 关于ini配置文件
 * @Authtor guoxiang
 * @Date 2020/5/6 19:52
 **/
public class ShiroIniFileTest {

    /**
     * 测试*.ini配置文件只带有[users]和[roles]的属性配置
     */
    @Test
    public void tsetByIniFileOnlyUsersRoles(){
        // 构造身份验证的token
        UsernamePasswordToken token = new UsernamePasswordToken("gx", "123456");
        login("classpath:shiro-users-roles.ini", token);
    }

    /**
     * 测试自定义realm - 实现Realm接口
     */
    @Test
    public void testByCustomRealm() {
        // 构造身份验证的token
        UsernamePasswordToken token = new UsernamePasswordToken("gx", "123456");
        login("classpath:shiro-custom-realm.ini", token);
    }

    /**
     * 测试JDBCRealm
     */
    @Test
    public void testJDBCRealm() {
        // 构造身份验证的token
        UsernamePasswordToken token = new UsernamePasswordToken("gx", "123456");
        login("classpath:shiro-jdbc-realm.ini", token);
    }

    /**
     * 测试多个自定义Realm的authenticationStrategy策略
     * FirstSuccessfulStrategy：只要有一个 Realm 验证成功即可，只返回第一个 Realm 身份验证成功的认证信息，其他的忽略；
     * AtLeastOneSuccessfulStrategy：只要有一个 Realm 验证成功即可，和 FirstSuccessfulStrategy 不同，返回所有 Realm 身份验证成功的认证信息；
     * AllSuccessfulStrategy：所有 Realm 验证成功才算成功，且返回所有 Realm 身份验证成功的认证信息，如果有一个失败就失败了。
     * 如果其中有一个realm不通过，会在控制台打印警告信息，
     * 如果全部不通过，会直接在控制台打印错误信息，并且不会往下执行；需要优先处理一下。
     */
    @Test
    public void testMultiRealm() {
        // 使用 FirstSuccessfulStrategy 策略，只要有一个Realm认证通过，就登陆成功
        // 构造身份验证的token - 用户名 + 密码
//        UsernamePasswordToken token = new UsernamePasswordToken("gx", "123456");
        // 构造身份验证的token - 手机号 + 密码
//        UsernamePasswordToken token = new UsernamePasswordToken("17607490517", "123456");
        // 构造身份验证的token - 邮箱 + 密码
        UsernamePasswordToken token = new UsernamePasswordToken("375870381@qq.com", "123456");
        login("classpath:shiro-multi-realm.ini", token);
    }

    public void login(String iniPath, AuthenticationToken token){
        // 使用 IniSecurityManagerFactory 根据 ini 配置文件创建一个 SecurityManager工厂
        IniFactorySupport<SecurityManager> factory = new IniSecurityManagerFactory(iniPath);
        // 获取 SecurityManager 实例
        SecurityManager securityManager = factory.getInstance();
        // 将 securityManager 设置到 SecurityUtils 中，方便全局使用
        SecurityUtils.setSecurityManager(securityManager);
        // 通过 SecurityUtils 得到 Subject，其会自动绑定到当前线程；如果在 web 环境在请求结束时需要解除绑定。
        // Subject 在 shiro 中一般表示用户
        Subject currentUser = SecurityUtils.getSubject();
        try {
            // 使用传过来已经构造好的身份验证token,进行登陆
            /**
             * 如果登陆失败，如果身份验证失败请捕获 AuthenticationException 或其子类，
             * 常见的如： DisabledAccountException（禁用的帐号）、LockedAccountException（锁定的帐号）、UnknownAccountException（错误的帐号）、
             * ExcessiveAttemptsException（登录失败次数过多）、IncorrectCredentialsException （错误的凭证）、ExpiredCredentialsException（过期的凭证）等，
             * 具体请查看其继承关系；
             */
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            System.out.println("用户名不存在:" + token.getPrincipal());
        } catch (IncorrectCredentialsException ice) {
            System.out.println("账户密码 " + token.getPrincipal()  + " 不正确!");
        }
        // 认证成功后
        if (currentUser.isAuthenticated()) {
            System.out.println("用户 " + currentUser.getPrincipal() + " 登陆成功！");
            //测试角色
            System.out.println("是否拥有 manager 角色：" + currentUser.hasRole("manager"));
            //测试权限
            System.out.println("是否拥有 user:create 权限" + currentUser.isPermitted("user:create"));
            //退出
            currentUser.logout();
        }
    }
}
