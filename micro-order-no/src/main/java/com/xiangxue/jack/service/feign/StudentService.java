package com.xiangxue.jack.service.feign;

import com.xiangxue.jack.bean.Student;

public interface StudentService {

    String getAllStudent();

    String saveStudent(Student student);

    String queryStudentById(Integer id);

    String errorMessage(Integer id);

    String queryStudentTimeout(int millis);
}
