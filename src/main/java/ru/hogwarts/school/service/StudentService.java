package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        student.setId(null);
        student.setVersion(null);
        return studentRepository.save(student);
    }

    public Optional<Student> getStudentById (Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findAll().stream().
                filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Collection<Student> findSudentsByAgeBetween(int age, int age2) {
        return studentRepository.findStudentByAgeBetween(age, age2);
    }

    public Faculty getFacultyById(Long facultyId) {
        Optional<Student> student = getStudentById(facultyId);
        return student.get().getFaculty();
    }
}
