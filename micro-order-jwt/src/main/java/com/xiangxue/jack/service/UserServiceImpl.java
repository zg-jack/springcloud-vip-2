package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ConsultContent;
import com.xiangxue.jack.bean.User;
import com.xiangxue.jack.config.JWT;
import com.xiangxue.jack.config.UserLoginDTO;
import com.xiangxue.jack.dao.CommonMapper;
import com.xiangxue.jack.dao.UserMapper;
import com.xiangxue.jack.feign.AuthServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;


//@Scope("refresh")
@Slf4j
@Service
public class UserServiceImpl implements UserService {

//	@Value("${xx.name:jack}")
//	private String name;

    @Autowired
    CommonMapper mapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthServiceClient client;

    @Autowired
    OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ConsultContent> queryContent() {
        return mapper.queryContent(new HashMap());
    }

    @Override
    public int insertUser(String username, String password) {
        String finalPassword = "{bcrypt}" + new BCryptPasswordEncoder().encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(finalPassword);
        return userMapper.saveUser(user);
    }

    @Override
    public UserLoginDTO login(String username, String password) {
        // 查询数据库
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("error username");
        }

//        if (!BPwdEncoderUtil.matches(password, user.getPassword())) {
//            throw new RuntimeException("error password");
//        }
        String client_secret = oAuth2ClientProperties.getClientId()+":"+oAuth2ClientProperties.getClientSecret();

        client_secret = "Basic "+Base64.getEncoder().encodeToString(client_secret.getBytes());
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization",client_secret);
//
//        //授权请求信息
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.put("username", Collections.singletonList(username));
//        map.put("password", Collections.singletonList(password));
//        map.put("grant_type", Collections.singletonList(oAuth2ProtectedResourceDetails.getGrantType()));
//
//        map.put("scope", oAuth2ProtectedResourceDetails.getScope());
//        //HttpEntity
//        HttpEntity httpEntity = new HttpEntity(map,httpHeaders);
//        //获取 Token
//        ResponseEntity<OAuth2AccessToken> exchange = restTemplate.exchange(oAuth2ProtectedResourceDetails.getAccessTokenUri(), HttpMethod.POST, httpEntity, OAuth2AccessToken.class);


        // 从auth-service获取JWT
        JWT jwt = client.getToken(client_secret, "password", username, password);
        if(jwt == null){
            throw new RuntimeException("error internal");
        }

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setJwt(jwt);
        userLoginDTO.setUser(user);
        return userLoginDTO;
    }
}
