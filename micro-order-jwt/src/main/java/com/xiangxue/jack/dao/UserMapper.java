package com.xiangxue.jack.dao;

import com.xiangxue.jack.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Insert("insert into user(id,username,password) values(#{id},#{username},#{password})")
    int saveUser(User user);

    @Select("select * from user where username=#{username}")
    User findByUsername(String username);
}
