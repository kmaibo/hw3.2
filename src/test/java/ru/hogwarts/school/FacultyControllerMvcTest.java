package ru.hogwarts.school;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    public void testGetAllFaculties() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Brown");
        Faculty faculty1 = new Faculty("Гриффиндор", "Green");


        mockMvc.perform(get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Слизерин"))
                .andExpect(jsonPath("$[1].name").value("Гриффиндор"));
    }

    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Brown");

        mockMvc.perform(get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Слизерин"));
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Brown");

        String json = """
                {
                    "name": "Слизерин",
                    "color": "Brown"
                }
                """;

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/faculties/1"));


    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty existingFaculty = new Faculty("Слизерин", "Green");
        Faculty updatedFaculty = new Faculty("Слизерин", "Blue");

        String json = """
                {
                    "title": "Слизерин",
                    "color": "Blue"
                }
                """;

        mockMvc.perform(put("/faculties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("С"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }
}