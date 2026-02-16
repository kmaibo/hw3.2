package ru.hogwarts.school;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @AfterEach
    void resetMocks() {
        reset(studentService);
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student = new Student("Alice", 20);
        Student student2 = new Student("Bob", 22);

        when(studentService.getAllStudents()).thenReturn(List.of(student, student2));

        mockMvc.perform(get("/student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].age").value(22));
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student("Alice", 20);
        student.setId(1L);

        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void testGetStudentByIdNotFound() throws Exception {
        when(studentService.getStudentById(999L)).thenReturn(null);

        mockMvc.perform(get("/student/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student savedStudent = new Student("Alice", 20);
        savedStudent.setId(1L);

        when(studentService.createStudent(any(Student.class))).thenReturn(savedStudent);

        String json = """
                {
                    "name": "Alice",
                    "age": 20
                }
                """;

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/student/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
