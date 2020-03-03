package com.xiangxue.jack.service.feign;

import com.xiangxue.jack.bean.Student;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceFallback implements StudentService{

    @Override
    public String getAllStudent() {
        return "获取所有学生信息失败";
    }

    @Override
    public String saveStudent(Student student) {
        return "保存学生信息失败";
    }

    @Override
    public String getStudentById(Integer id) {
        return "根据Id获取学生信息失败";
    }

    /*
    * 这种方式拿不到provider的具体异常信息
    * */
    @Override
    public String errorMessage(Integer id) {
        return "根据Id获取学生信息失败";
    }

    @Override
    public String queryStudentTimeout(int millis) {
        return "queryStudentTimeout信息失败";
    }
}
