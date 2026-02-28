package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentGetSqlRepository;

import java.util.List;

@Service
public class StudentGetService {

    Logger log = LoggerFactory.getLogger(StudentGetService.class);

    private StudentGetSqlRepository studentGetSqlRepository;

    public StudentGetService(StudentGetSqlRepository studentGetSqlRepository) {
        this.studentGetSqlRepository = studentGetSqlRepository;
    }

    public Integer getCountStudent() {
        log.info("Was invoked method for get count student");
        return studentGetSqlRepository.getAllStudents();
    }

    public Integer getAverageAgeStudents() {
        log.info("Was invoked method for get average age students");
        return studentGetSqlRepository.getAverageAgeStudents();
    }

    public List<Student> getFiveLastStudents() {
        log.info("Was invoked method for get five last students");
        return studentGetSqlRepository.getLastFiveStudents();
    }
}
