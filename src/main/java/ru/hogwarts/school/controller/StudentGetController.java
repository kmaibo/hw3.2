package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentGetService;

import java.util.List;

@RestController
public class StudentGetController {

    private StudentGetService studentGetService;

    public StudentGetController(StudentGetService studentGetService) {
        this.studentGetService = studentGetService;
    }

    @GetMapping("/student-count")
    public int getStudentCount() {
        return studentGetService.getCountStudent();
    }

    @GetMapping("/student-average-age")
    public double getStudentAverageAge() {
        return studentGetService.getAverageAgeStudents();
    }

    @GetMapping("/get-five-last-student")
    public List<Student> getFiveLastStudents() {
        return studentGetService.getFiveLastStudents();
    }
}
