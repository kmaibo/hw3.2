package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;

import java.util.List;



@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerRestTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate.delete("/faculty");
    }


    @Test
    public void testGetAllFaculties() {
        Faculty faculty = new Faculty("Слизерин", "Brown");
        Faculty faculty1 = new Faculty("Гриффиндор", "Green");

        restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        restTemplate.postForEntity("/faculty", faculty1, Faculty.class);

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "/faculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Информатика");
    }

    @Test
    public void testGetFacultyById() {
        Faculty created = restTemplate.postForObject(
                "/faculty", new Faculty("Слизерин","Brown"), Faculty.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty" + "/" + created.getId(), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty("Слизерин", "Brown");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "/faculty", faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");

    }

    @Test
    public void testUpdateFaculty() {
        Faculty created = restTemplate.postForObject(
                "/faculty", new Faculty("Слизерин", "Brown"), Faculty.class);

        Faculty updated = new Faculty("Слизерин", "Blue");
        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty" + "/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updated),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");

    }

    @Test
    public void testDeleteFaculty() {
        Faculty created = restTemplate.postForObject(
                "/faculty", new Faculty("Слизерин", "Brown"), Faculty.class);


        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculty" + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                "/faculty" + "/" + created.getId(), Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
