package ru.hogwarts.school;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;


@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    public void testGetAllStudents() throws Exception {
        Student student = new Student("Alice", 20);
        Student student2 = new Student("Bob", 22);

        mockMvc.perform(get("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student("Alice", 20);
        mockMvc.perform(get("/students/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student savedStudent = new Student("Alice", 20);
        given(studentService.createStudent(any(Student.class))).willReturn(savedStudent);

        String json = """
            {
                "name": "Alice",
                "age": 20
            }
            """;

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/students/1"));

    }

    @Test
    public void testUpdateStudent() throws Exception {
        String updatedStudent = "{\"name\": \"Пётр\", \"age\": 21}";
        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedStudent))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNoContent());
    }
}