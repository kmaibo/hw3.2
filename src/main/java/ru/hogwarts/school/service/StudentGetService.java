package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentGetSqlRepository;

import java.util.List;

@Service
public class StudentGetService {

    private StudentGetSqlRepository studentGetSqlRepository;

    public StudentGetService(StudentGetSqlRepository studentGetSqlRepository) {
        this.studentGetSqlRepository = studentGetSqlRepository;
    }

    public Integer getCountStudent() {
        return studentGetSqlRepository.getAllStudents();
    }

    public Integer getAverageAgeStudents() {
        return studentGetSqlRepository.getAverageAgeStudents();
    }

    public List<Student> getFiveLastStudents() {
        return studentGetSqlRepository.getLastFiveStudents();
    }
}
