package com.example.springbootshiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //增加shiro的内置过滤器
        /*
            anon：无需认证即可访问，
            authc：必须认证才能访问，
            user：必须拥有“记住我”功能才能访问，
            perms：拥有对某个资源的权限才能访问，
            role：拥有某个角色权限才能访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/add","perms[user:add]");
        filterMap.put("/user/update","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        shiroFilterFactoryBean.setLoginUrl("/login");

        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");

        return shiroFilterFactoryBean;
    }


    //DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("myRealm") MyRealm myRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        defaultWebSecurityManager.setRealm(myRealm);

        return defaultWebSecurityManager;
    }


    //realm
    @Bean
    public MyRealm myRealm(){

        return new MyRealm();
    }

    //整合shiro和thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    //配置druid数据源
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();

        return dataSource;
    }

    /**
     * 配置 Druid 监控界面
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean srb =
                new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //设置控制台管理用户
        srb.addInitParameter("loginUsername","root");
        srb.addInitParameter("loginPassword","root");
        //是否可以重置数据
        srb.addInitParameter("resetEnable","false");
        return srb;
    }

    //WebStatFilter()用于采集 web-jdbc关联监控的数据
    @Bean
    public FilterRegistrationBean statFilter(){
        //创建过滤器
        FilterRegistrationBean frb =
                new FilterRegistrationBean(new WebStatFilter());
        //设置过滤器过滤路径
        frb.addUrlPatterns("/*");
        //忽略过滤的形式
        frb.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return frb;
    }

}
