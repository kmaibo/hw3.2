package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StudentControllerRestTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetAllStudents() {
        testRestTemplate.postForEntity("/student", new Student("Alice", 16), Student.class);
        testRestTemplate.postForEntity("/student", new Student("Bob", 17), Student.class);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                "/student", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Student>>() {
                }
        );
    }

    @Test
    public void testGetStudentById() {
        Student created = testRestTemplate.postForObject(
                "/student", new Student("Alice", 20), Student.class);

        ResponseEntity<Student> response = testRestTemplate.getForEntity(
                "/student/" + created.getId(), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Alice");
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student("Alice", 20);

        ResponseEntity<Student> response = testRestTemplate.postForEntity("/student", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Alice");
    }

    @Test
    public void testDeleteStudent() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/student/1",
                HttpMethod.DELETE,
                null,
                void.class
        );
        assertEquals(HttpStatus.NO_CONTENT,  response.getStatusCode());
    }
}
