package com.xiangxue.jack.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.xiangxue.jack.bean.Student;
import com.xiangxue.jack.dao.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public String getAllStudent() {
        String result = JSONObject.toJSONString(studentMapper.getAllStudent());
        return result;
    }

    @Override
    public String saveStudent(Student student) {
        int i = studentMapper.saveStudent(student);
        if(i == 1) {
            return "S";
        }
        return "F";
    }

    @Override
    public String queryStudentById(Integer id) {
        Student student = studentMapper.queryStudentById(id);
        return JSONObject.toJSONString(student);
    }

    @Override
    public String errorMessage(Integer id) {
        try {
            Student student = studentMapper.queryStudentById(id);
            int a = 1 / 0;
            return JSONObject.toJSONString(student);
        }catch (Exception e) {
            e.printStackTrace();
//            return e.getMessage();
            throw e;
        }
    }

    @Override
    public String queryStudentTimeout(int millis) {
        return null;
    }
}
