package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentGetSqlRepository extends JpaRepository<Student, Integer> {

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    Integer getAllStudents();

    @Query(value = "SELECT avg(age) from student", nativeQuery = true)
    Integer getAverageAgeStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();
}
