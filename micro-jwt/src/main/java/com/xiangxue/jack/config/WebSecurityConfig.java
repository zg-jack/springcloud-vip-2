package com.xiangxue.jack.config;

import com.xiangxue.jack.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceDetail userServiceDetail;

    @Override
    public @Bean
    AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
    *   access(String) 如果给定的SpEL表达式计算结果为true，就允许访问
        anonymous() 允许匿名用户访问
        authenticated() 允许认证的用户进行访问
        denyAll() 无条件拒绝所有访问
        fullyAuthenticated() 如果用户是完整认证的话（不是通过Remember-me功能认证的），就允许访问
        hasAuthority(String) 如果用户具备给定权限的话就允许访问
        hasAnyAuthority(String…)如果用户具备给定权限中的某一个的话，就允许访问
        hasRole(String) 如果用户具备给定角色(用户组)的话,就允许访问/
        hasAnyRole(String…) 如果用户具有给定角色(用户组)中的一个的话,允许访问.
        hasIpAddress(String 如果请求来自给定ip地址的话,就允许访问.
        not() 对其他访问结果求反.
        permitAll() 无条件允许访问
        rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
    *
    * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//关闭CSRF
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").permitAll()
////                .antMatchers("/**").authenticated()
//                .and()
//                .httpBasic();
        http.requestMatchers().anyRequest()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public static NoOpPasswordEncoder noOpPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetail).passwordEncoder(passwordEncoder());
    }
}
