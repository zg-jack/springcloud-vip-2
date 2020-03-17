package com.xiangxue.jack.controller;

import com.xiangxue.jack.api.TeacherService;
import com.xiangxue.jack.bean.Teacher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeacherController implements TeacherService {
    @Override
    public String getAllTeacher() {
        return "micro-order.getAllTeacher";
    }

    @Override
    public String saveTeacher(@RequestBody Teacher Teacher) {
        return "micro-order.saveTeacher";
    }

    @Override
    public String getTeacherById(@RequestParam("id") Integer id) {
        return "micro-order.getTeacherById";
    }

    @Override
    public String getTeacherByName(@PathVariable("name") String name) {
        return "micro-order.getTeacherByName";
    }

    @Override
    public String errorMessage(@RequestParam("id") Integer id) {
        return "micro-order.errorMessage";
    }
}
