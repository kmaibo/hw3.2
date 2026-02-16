package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findAll().stream().
                filter(f -> f.getColor().equals(color))
                .collect(Collectors.toList());
    }

    public Collection<Faculty> findFacultiesByColorContainingIgnoreCaseOrNameContainingIgnoreCase(String color, String name) {
        return facultyRepository.findFacultiesByColorContainingIgnoreCaseOrNameContainingIgnoreCase(color, name);
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        Optional<Faculty> faculty = getFacultyById(facultyId);
        return faculty.get().getStudents();
    }
}