package ru.hogwarts.school;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class FacultyControllerRestTest {


    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testGetAllFaculties() {
        ResponseEntity<String> response = restTemplate.getForEntity("/faculties", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetFacultyById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/faculties/1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateFaculty() {
        String newFaculty = "{\"title\": \"Информатика\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/faculties", newFaculty, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdateFaculty() {
        String updatedFaculty = "{\"title\": \"Прикладная информатика\"}";
        ResponseEntity<String> response = restTemplate.exchange(
                "/faculties/1",
                HttpMethod.PUT,
                new HttpEntity<>(updatedFaculty),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteFaculty() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculties/1",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
