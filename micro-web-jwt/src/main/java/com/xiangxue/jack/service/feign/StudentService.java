package com.xiangxue.jack.service.feign;

import com.xiangxue.jack.bean.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/*
* fallback = StudentServiceFallback.class
* 不能获取具体异常
*
* */
@FeignClient(name = "MICRO-ORDER",path = "/feign"
        /*fallback = StudentServiceFallback.class,*/
        ,fallbackFactory = StudentServiceFallbackFactory.class)
public interface StudentService {

    @GetMapping("/student/getAllStudent")
    String getAllStudent();

    @PostMapping("/student/saveStudent")
    String saveStudent(@RequestBody Student student);

    @GetMapping("/student/getStudentById")
    String getStudentById(@RequestParam("id") Integer id);

    @GetMapping("/student/errorMessage")
    String errorMessage(@RequestParam("id") Integer id);

    @GetMapping("/student/queryStudentTimeout")
    String queryStudentTimeout(@RequestParam("millis") int millis);
}
