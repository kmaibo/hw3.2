package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        logger.info("Was invoked method for get faculty by id");
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFacultyById(Long id) {
        logger.info("Was invoked method for get faculty by id");
        return facultyRepository.findById(id);
    }

    public List<Faculty> getAllFaculty() {
        logger.info("Was invoked method for get all faculty");
        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for update faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculties by color");
        return facultyRepository.findAll().stream().
                filter(f -> f.getColor().equals(color))
                .collect(Collectors.toList());
    }

    public Collection<Faculty> findFacultiesByColorContainingIgnoreCaseOrNameContainingIgnoreCase(String color, String name) {
        logger.info("Was invoked method for get faculties by color and name containing");
        return facultyRepository.findFacultiesByColorContainingIgnoreCaseOrNameContainingIgnoreCase(color, name);
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method for get faculty by id " + facultyId);
        Optional<Faculty> faculty = getFacultyById(facultyId);
        return faculty.get().getStudents();
    }
}