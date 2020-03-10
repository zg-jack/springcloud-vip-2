package com.xiangxue.jack.dao;

import com.xiangxue.jack.bean.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    @Select("select * from xx_student")
    List<Student> getAllStudent();

    @Select("select * from xx_student where id=#{id}")
    Student queryStudentById(Integer id);

    @Insert("insert into xx_student(id,card_num,name) values(#{id},#{card_num},#{name})")
    int saveStudent(Student student);
}
