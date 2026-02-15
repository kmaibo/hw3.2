package ru.hogwarts.school;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void testGetAllFaculties() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Brown");
        Faculty faculty1 = new Faculty("Гриффиндор", "Green");

        when(facultyService.getAllFaculty()).thenReturn(List.of(faculty, faculty1));

        mockMvc.perform(get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Слизерин"))
                .andExpect(jsonPath("$[1].name").value("Гриффиндор"));
    }

    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Brown");
        faculty.setId(1L);

        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Слизерин"));
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Brown");

        when(facultyService.create(any(Faculty.class))).thenReturn(faculty);

        String json = """
                {
                    "name": "Слизерин",
                    "color": "Brown"
                }
                """;

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty existingFaculty = new Faculty("Слизерин", "Green");
        Faculty updatedFaculty = new Faculty("Слизерин", "Blue");

        when(facultyService.updateFaculty( any(Faculty.class)))
                .thenReturn(updatedFaculty);

        String json = """
                {
                    "name": "Слизерин",
                    "color": "Blue"
                }
                """;

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }
}