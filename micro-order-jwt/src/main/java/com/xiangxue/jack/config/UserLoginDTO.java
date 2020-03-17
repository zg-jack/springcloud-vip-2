package com.xiangxue.jack.config;

import com.xiangxue.jack.bean.User;
import lombok.Data;

@Data
public class UserLoginDTO {
    private JWT jwt;
    private User user;
}
