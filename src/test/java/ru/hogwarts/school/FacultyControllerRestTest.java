package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
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
@AutoConfigureTestRestTemplate
public class FacultyControllerRestTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        ResponseEntity<List<Faculty>> allFaculties = restTemplate.exchange(
                "/faculty", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );
        if (allFaculties.getBody() != null) {
            for (Faculty faculty : allFaculties.getBody()) {
                restTemplate.delete("/faculty/" + faculty.getId());
            }
        }
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
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);

        Faculty firstFaculty = response.getBody().get(0);
        Faculty secondFaculty = response.getBody().get(1);

        assertThat(firstFaculty.getName()).isEqualTo("Слизерин");
        assertThat(firstFaculty.getColor()).isEqualTo("Brown");
        assertThat(secondFaculty.getName()).isEqualTo("Гриффиндор");
        assertThat(secondFaculty.getColor()).isEqualTo("Green");
    }

    @Test
    public void testGetFacultyById() {
        Faculty created = restTemplate.postForObject(
                "/faculty", new Faculty("Слизерин", "Brown"), Faculty.class
        );

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/" + created.getId(), Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Faculty faculty = response.getBody();
        assertThat(faculty.getId()).isEqualTo(created.getId());
        assertThat(faculty.getName()).isEqualTo("Слизерин");
        assertThat(faculty.getColor()).isEqualTo("Brown");
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty("Слизерин", "Brown");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "/faculty", faculty, Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        Faculty createdFaculty = response.getBody();
        assertThat(createdFaculty.getId()).isNotNull();
        assertThat(createdFaculty.getName()).isEqualTo("Слизерин");
        assertThat(createdFaculty.getColor()).isEqualTo("Brown");
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().getPath())
                .endsWith("/faculty/" + createdFaculty.getId());
    }

    @Test
    public void testUpdateFaculty() {
        Faculty created = restTemplate.postForObject(
                "/faculty", new Faculty("Слизерин", "Brown"), Faculty.class
        );

        Faculty updated = new Faculty("Слизерин", "Blue");

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updated),
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Faculty updatedFaculty = response.getBody();
        assertThat(updatedFaculty.getId()).isEqualTo(created.getId());
        assertThat(updatedFaculty.getName()).isEqualTo("Слизерин");
        assertThat(updatedFaculty.getColor()).isEqualTo("Blue");
    }

    @Test
    public void testDeleteFaculty() {
        Faculty created = restTemplate.postForObject(
                "/faculty", new Faculty("Слизерин", "Brown"), Faculty.class
        );

        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculty/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode())
                .isIn(HttpStatus.NO_CONTENT, HttpStatus.OK);

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                "/faculty/" + created.getId(), Faculty.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getBody()).isNull();
    }

    @Test
    public void testGetNonExistentFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/999", Faculty.class
        );
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}
