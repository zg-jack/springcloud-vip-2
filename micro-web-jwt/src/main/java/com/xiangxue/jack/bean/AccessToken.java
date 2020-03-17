package com.xiangxue.jack.bean;

import lombok.Data;

@Data
public class AccessToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
}
