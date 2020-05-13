package com.gx.shiro.config;

import com.gx.shiro.realm.CustomRealmCache;
import com.gx.shiro.realm.CustomRealmExtendAuthorizingRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description Shiro的配置类，代替xml文件
 * @Authtor guoxiang
 * @Date 2020/5/7 14:39
 **/
// 从Spring3.0，@Configuration用于定义配置类，可替换xml配置文件
// 参考：https://www.cnblogs.com/duanxz/p/7493276.html
@Configuration
public class ShiroConfig {

    // @Bean注解在返回实例的方法上，name用来指定Bean的名称，默认与标注的方法名相同；
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuthorized");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问; user:表示设置了记住我的可以访问

        filterChainDefinitionMap.put("/user/delete", "authc");
        // 其他的只要设置了记住我就可以访问
        filterChainDefinitionMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    // SecurityManager的Bean对象
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        // 设置自定义realm对象
        defaultSecurityManager.setRealm(customRealm());
        // 设置缓存管理器 -- 注意如果设置了chacheManager,则会自动缓存授权信息，但是认证信息不会缓存
        defaultSecurityManager.setCacheManager(cacheManager());
        // 设置rememberMe管理器
        defaultSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultSecurityManager;
    }

    // 自定义的Realm的Bean对象
//    @Bean
//    public CustomRealmExtendAuthorizingRealm customRealm() {
//        // 创建一个自定义的Realm，并返回
//        CustomRealmExtendAuthorizingRealm customRealm = new CustomRealmExtendAuthorizingRealm();
//        return customRealm;
//    }

    // 自定义的Realm的Bean对象 -- 带有缓存机制的
    @Bean
    public CustomRealmCache customRealm() {
        // 创建一个自定义的Realm，并返回
        CustomRealmCache customRealm = new CustomRealmCache();
        // 启用缓存，默认 false  -- 如果SecurityManager设置了CacheManager，该功能会自动打开
        customRealm.setCachingEnabled(true);
        // 启用身份验证缓存，即缓存 AuthenticationInfo 信息，默认 false；
        customRealm.setAuthenticationCachingEnabled(true);
        // 缓存 AuthenticationInfo 信息的缓存名称；
        customRealm.setAuthenticationCacheName("authentication");
        // 启用授权缓存，即缓存 AuthorizationInfo 信息，默认 false；
        customRealm.setAuthorizationCachingEnabled(true);
        // 缓存 AuthorizationInfo 信息的缓存名称；
        customRealm.setAuthorizationCacheName("authorization");
        return customRealm;
    }

    /**
     * cacheManager：缓存管理器，此处使用 EhCacheManage
     * @return
     */
    @Bean
    public EhCacheManager cacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * rememberMe的Cookie
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        // 这里必须要给一个Cookie名称，不然在Spring自动注入的时候报错 Cookie name cannot be null/empty
        SimpleCookie rememberMeCookie = new SimpleCookie("rememberMe");
        rememberMeCookie.setHttpOnly(true);
        // 设置过期时间，单位为S, 默认是-1，表示关闭浏览器就失效
        rememberMeCookie.setMaxAge(120);
        return  rememberMeCookie;
    }

    /**
     * rememberMe管理器
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        return rememberMeManager;
    }

    /**
     * 管理shiro中bean的生命周期
     * 一般与 DefaultAdvisorAutoProxyCreator Bean一起被使用
     */

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * DefaultAdvisorAutoProxyCreator是用来扫描上下文，寻找所有的Advistor(通知器），将这些Advisor应用到所有符合切入点的Bean中。
     * 所以必须在lifecycleBeanPostProcessor创建之后创建,所以加@DependsOn({"lifecycleBeanPostProcessor"})
     *
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * 参考：https://blog.csdn.net/qq_36850813/article/details/93750520
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * AuthorizationAttributeSourceAdvisor匹配所有类，匹配所有加了认证注解的方法
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}

