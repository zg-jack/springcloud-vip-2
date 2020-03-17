package com.xiangxue.jack.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
//启用全局方法安全注解，就可以在方法上使用注解来对请求进行过滤
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

//    @Autowired
//    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // 配置order访问控制，必须认证后才可以访问
        http.authorizeRequests()
                .antMatchers("/order/**","/user/**").authenticated();
    }

    /*
    * 把token验证失败后，重新刷新token的类设置到 OAuth2AuthenticationProcessingFilter
    * token验证过滤器中
    * */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
//        resources.authenticationEntryPoint(new RefreshTokenAuthenticationEntryPoint());
//        resources.tokenStore(tokenStore);
    }
}
