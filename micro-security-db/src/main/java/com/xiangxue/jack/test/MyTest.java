package com.xiangxue.jack.test;

import com.xiangxue.jack.MicroSecurityDbApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroSecurityDbApplication.class)
@WebAppConfiguration
public class MyTest {

    @Test
    public void test1() {
        String a = "{bcrypt}" + new BCryptPasswordEncoder().encode("123");
        System.out.println(a);
    }
}
