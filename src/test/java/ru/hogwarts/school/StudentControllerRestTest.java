package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate

public class StudentControllerRestTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        testRestTemplate.delete("/student");
        studentRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        ResponseEntity<List<Student>> allStudents = testRestTemplate.exchange(
                "/student", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );
        if (allStudents.getBody() != null) {
            for (Student student : allStudents.getBody()) {
                testRestTemplate.delete("/student/" + student.getId());
            }
        }
    }

    @Test
    public void testGetAllStudents() {
        testRestTemplate.postForEntity("/student", new Student("Alice", 16), Student.class);
        testRestTemplate.postForEntity("/student", new Student("Bob", 17), Student.class);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                "/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);

        Student firstStudent = response.getBody().get(0);
        Student secondStudent = response.getBody().get(1);

        assertThat(firstStudent.getName()).isEqualTo("Alice");
        assertThat(firstStudent.getAge()).isEqualTo(16);
        assertThat(secondStudent.getName()).isEqualTo("Bob");
        assertThat(secondStudent.getAge()).isEqualTo(17);
    }

    @Test
    public void testGetStudentById() {
        Student created = testRestTemplate.postForObject(
                "/student", new Student("Alice", 20), Student.class
        );

        ResponseEntity<Student> response = testRestTemplate.getForEntity(
                "/student/" + created.getId(), Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Student student = response.getBody();
        assertThat(student.getId()).isEqualTo(created.getId());
        assertThat(student.getName()).isEqualTo("Alice");
        assertThat(student.getAge()).isEqualTo(20);
    }

    @Test
    public void testGetNonExistentStudent() {
        ResponseEntity<Student> response = testRestTemplate.getForEntity(
                "/student/999", Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();

    }

    @Test
    public void testCreateStudent() {
        Student student = new Student("Alice", 20);

        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                "/student", student, Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        Student createdStudent = response.getBody();
        assertThat(createdStudent.getId()).isNotNull();
        assertThat(createdStudent.getName()).isEqualTo("Alice");
        assertThat(createdStudent.getAge()).isEqualTo(20);

        // Проверка заголовка Location
        assertThat(response.getHeaders().getLocation())
                .hasPath("/student/" + createdStudent.getId());
    }

    @Test
    public void testDeleteStudent() {
        Student created = testRestTemplate.postForObject(
                "/student", new Student("Alice", 20), Student.class
        );

        ResponseEntity<Void> deleteResponse = testRestTemplate.exchange(
                "/student/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse).isNotNull();
        assertThat(deleteResponse.getStatusCode())
                .isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);

        ResponseEntity<Student> getResponse = testRestTemplate.getForEntity(
                "/student/" + created.getId(), Student.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void testDeleteNonExistentStudent() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/student/999",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode())
                .isIn(HttpStatus.NOT_FOUND, HttpStatus.NO_CONTENT);
    }
}