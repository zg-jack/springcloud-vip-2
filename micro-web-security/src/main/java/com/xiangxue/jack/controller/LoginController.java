package com.xiangxue.jack.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiangxue.jack.bean.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    String oauthServiceUrl = "http://api-gateway/auth/oauth/token";

    @GetMapping("/toAuthLogin")
    public void toAuthLogin(HttpServletResponse response) throws IOException {

        String redirectUrl = "http://localhost:3030/oauth/authorize?"
                +"client_id=pc&"
                +"redirect_uri=http://localhost:8083/login/callback&"
                +"response_type=code&"
                +"state=/index"; //state参数传过去啥传回来啥，一般记录跳转之前的路径
        response.addHeader("Authorization","Basic amFjazoxMjM0NTY=");
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, String state , HttpSession session){

        log.info("code is {}, state is {}",code,state);

        //认证服务器验token地址 /oauth/check_token 是  spring .security.oauth2的验token端点
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);//不是json请求
        //网关的appId，appSecret，需要在数据库oauth_client_details注册
        headers.setBasicAuth("pc","123456");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("code",code);//授权码
        params.add("grant_type","authorization_code");//授权类型-授权码模式
        //认证服务器会对比数据库客户端信息的的redirect_uri和这里的是不是一致，不一致就报错
        params.add("redirect_uri","http://localhost:8083/login/callback");

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,headers);
        ResponseEntity<AccessToken> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, AccessToken.class);

        session.setAttribute("token",response.getBody());

        return JSONObject.toJSONString(response.getBody());
    }
}
