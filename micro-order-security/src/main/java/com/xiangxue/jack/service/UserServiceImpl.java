package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ConsultContent;
import com.xiangxue.jack.dao.CommonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ConsultContent> queryContent() {
        return mapper.queryContent(new HashMap());
    }

}
