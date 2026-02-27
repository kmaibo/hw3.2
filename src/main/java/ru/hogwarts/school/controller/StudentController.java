package ru.hogwarts.school.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student created = studentService.createStudent(student);

        URI location = URI.create("/student/" + created.getId());

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Student>> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);

        if (student != null && student.isPresent()) {
            return ResponseEntity.ok(Optional.of(student.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudent(@RequestParam(required = false) Integer age,
                                                             @RequestParam (required = false) Integer age2) {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/age/{age}")
    public List<Student> getStudentsByAge(@PathVariable int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyById(id);
        return ResponseEntity.ok(faculty);
    }
}