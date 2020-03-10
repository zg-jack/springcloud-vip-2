package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.Student;
import com.xiangxue.jack.service.feign.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StudentController implements StudentService {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/feign/student/getAllStudent")
    @Override
    public String getAllStudent() {
        return studentService.getAllStudent();
    }

    @RequestMapping("/feign/student/getStudentById")
    @Override
    public String queryStudentById(@RequestParam("id") Integer id) {
        return studentService.queryStudentById(id);
    }

    @RequestMapping("/feign/student/saveStudent")
    @Override
    public String saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @RequestMapping("/feign/student/errorMessage")
    @Override
    public String errorMessage(@RequestParam("id") Integer id) {
        return studentService.errorMessage(id);
    }

    @RequestMapping("/feign/student/queryStudentTimeout")
    @Override
    public String queryStudentTimeout(@RequestParam("millis") int millis) {
        log.info("provider--->" + millis);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "provider--->" + millis;
    }
}
