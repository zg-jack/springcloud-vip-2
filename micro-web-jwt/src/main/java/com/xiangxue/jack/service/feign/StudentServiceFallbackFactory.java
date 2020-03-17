package com.xiangxue.jack.service.feign;

import com.xiangxue.jack.bean.Student;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentServiceFallbackFactory implements FallbackFactory<StudentService> {

    @Override
    public StudentService create(Throwable throwable) {

        if(throwable == null) {
            return null;
        }
        final String msg = throwable.getMessage();
        log.info("exception:" + msg);
        return new StudentService() {
            @Override
            public String getAllStudent() {
                log.info("exception=====getAllStudent==========" + msg);
                return msg;
            }

            @Override
            public String saveStudent(Student student) {
                log.info("exception=====saveStudent==========" + msg);
                return msg;
            }

            @Override
            public String getStudentById(Integer id) {
                log.info("exception=====getStudentById==========" + msg);
                return msg;
            }

            @Override
            public String errorMessage(Integer id) {
                log.info("exception=====errorMessage==========" + msg);
                return msg;
            }

            @Override
            public String queryStudentTimeout(int millis) {
                log.info("exception=====queryStudentTimeout==========" + msg);
                return msg;
            }
        };
    }
}
