package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.Teacher;
import com.xiangxue.jack.service.feign.TeacherServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherServiceFeign teacherService;

    @RequestMapping("/getAllTeacher")
    public String getAllTeacher() {
        return teacherService.getAllTeacher();
    }

    @RequestMapping("/saveTeacher")
    public String saveTeacher(@RequestBody Teacher teacher) {
        return teacherService.saveTeacher(teacher);
    }

    @RequestMapping("/getTeacherById")
    public String getTeacherById(Integer id) {
        return teacherService.getTeacherById(id);
    }

    @RequestMapping("/getTeacherByName/{name}")
    public String getTeacherByName(@PathVariable("name") String name) {
        return teacherService.getTeacherByName(name);
    }

    @RequestMapping("/errorMessage")
    public String errorMessage(Integer id) {
        return teacherService.errorMessage(id);
    }
}
