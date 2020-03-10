package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ConsultContent;
import com.xiangxue.jack.config.UserLoginDTO;

import java.util.List;


public interface UserService {
	public List<ConsultContent> queryContent();

	public int insertUser(String username, String  password);

	public UserLoginDTO login(String username, String password);
}
