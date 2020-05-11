package com.gx.shiro.config;

import com.gx.shiro.realm.CustomRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//        filterChainDefinitionMap.put("/webjars/**", "anon");
//        filterChainDefinitionMap.put("/login", "anon");
//        filterChainDefinitionMap.put("/", "anon");
//        filterChainDefinitionMap.put("/front/**", "anon");
//        filterChainDefinitionMap.put("/api/**", "anon");
//
//        filterChainDefinitionMap.put("/admin/**", "authc");
//        filterChainDefinitionMap.put("/user/**", "authc");
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
//        filterChainDefinitionMap.put("/**", "authc");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    // SecurityManager的Bean对象
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        // 设置自定义realm对象
        defaultSecurityManager.setRealm(customRealm());
        return defaultSecurityManager;
    }

    // 自定义的Realm的Bean对象
    @Bean
    public CustomRealm customRealm() {
        // 创建一个自定义的Realm，并返回
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
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

