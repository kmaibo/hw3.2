package ru.hogwarts.school.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;
    private StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
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
                                                             @RequestParam(required = false) Integer age2) {
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

    @GetMapping("/names-starting-with-a")
    public List<String> getAllNamesStartingWithA() {
        return studentService.getAllStudents().stream().map(Student::getName)
                .filter(name -> name != null && name.toUpperCase().startsWith("A"))
                .map(String::toUpperCase).sorted().collect(Collectors.toList());
    }

    @GetMapping("/average-age")
    public Double getAverageAge() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return 0.0;
        }

        return students.stream().mapToInt(Student::getAge)
                .average().orElse(0.0);
    }

    @GetMapping("/longest-faculty-name")
    public String getLongestFacultyName() {
        return studentRepository.findAll().stream()
                .map(Student::getFaculty)
                .filter(Objects::nonNull)
                .map(Faculty::getName).
                filter(name -> name != null && !name.trim().isEmpty())
                .max(Comparator.comparing(String::length))
                .orElse("");
    }

    @GetMapping("/optimized-sum")
    public long getOptimizedSum() {
        long n = 1000000L;
        return n * (n + 1) / 2;
    }
}