package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    Logger log = Logger.getLogger(StudentService.class.getName());

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        log.info("Creating student " + student);
        student.setId(null);
        student.setVersion(null);
        return studentRepository.save(student);
    }

    public Optional<Student> getStudentById (Long id) {
        log.info("Was invoked method for get student by id " + id);
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudents() {
        log.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public void deleteStudent(Long id) {
        log.info("Was invoked method for delete student by id " + id);
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAge(int age) {
        log.info("Was invoked method for get student by age " + age);
        return studentRepository.findAll().stream().
                filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Collection<Student> findSudentsByAgeBetween(int age, int age2) {
        log.info("Was invoked method for get student by age between " + age + " and " + age2);
        return studentRepository.findStudentByAgeBetween(age, age2);
    }

    public Faculty getFacultyById(Long facultyId) {
        log.info("Was invoked method  for get faculty by id " + facultyId);
        Optional<Student> student = getStudentById(facultyId);
        return student.get().getFaculty();
    }
}
