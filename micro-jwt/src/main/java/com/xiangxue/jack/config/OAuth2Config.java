package com.xiangxue.jack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/*
* keytool -genkeypair -alias micro-jwt
          -validity 3650
          -keyalg RSA
          -dname "CN=jwt,OU=jtw,O=jwt,L=zurich,S=zurich, C=CH"
          -keypass 123456
          -keystore micro-jwt.keystore
          -storepass 123456
*
* */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 将客户端的信息存储在内存中
        clients.inMemory()
                // 配置一个客户端
                .withClient("micro-order")
                .secret("123456")
                // 配置客户端的域
                .scopes("service")
                // 配置验证类型为refresh_token和password
                .authorizedGrantTypes("refresh_token", "password")
                // 配置token的过期时间为1h
                .accessTokenValiditySeconds(30);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置token的存储方式为JwtTokenStore
        endpoints.tokenStore(tokenStore())
                // 配置用于JWT私钥加密的增强器
                .tokenEnhancer(jwtTokenEnhancer())
                // 配置安全认证管理
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        // 配置jks文件
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("micro-jwt.jks"), "123456".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("micro-jwt"));
        return converter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        // 对获取Token的请求不再拦截
        oauthServer.tokenKeyAccess("permitAll()")
                // 验证获取Token的验证信息
                .checkTokenAccess("isAuthenticated()");
    }
}
