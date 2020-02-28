package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.Student;
import com.xiangxue.jack.service.feign.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
