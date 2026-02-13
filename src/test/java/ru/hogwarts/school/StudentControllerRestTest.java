package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class StudentControllerRestTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetAllStudents() {
        ResponseEntity<String> response =  testRestTemplate.getForEntity("/students", String.class);
        assertEquals (HttpStatus.OK,  response.getStatusCode());
    }

    @Test
    public void testGetStudentById() {
        ResponseEntity<String> response =  testRestTemplate.getForEntity("/students/1", String.class);
        assertEquals (HttpStatus.OK,  response.getStatusCode());
    }

    @Test
    public void testCreateStudent() {
        String newStudent = "{\"name\" : \"Иван\", \"age\" : 20}";
        ResponseEntity<String> response = testRestTemplate.postForEntity("/students", newStudent, String.class);
        assertEquals (HttpStatus.CREATED,  response.getStatusCode());
    }

    @Test
    public void testUpdateStudent() {
        String updatedStudent = "{\"name\": \"Петр\", \"age\": 21}";
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/students/1",
                HttpMethod.PUT,
                new HttpEntity<>(updatedStudent),
                String.class);
        assertEquals (HttpStatus.OK,  response.getStatusCode());
    }

    @Test
    public void testDeleteStudent() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/students/1",
                HttpMethod.DELETE,
                null,
                void.class
        );
        assertEquals(HttpStatus.NO_CONTENT,  response.getStatusCode());
    }
}
