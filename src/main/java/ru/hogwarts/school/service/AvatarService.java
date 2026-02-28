package ru.hogwarts.school.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class AvatarService {

    Logger logger = Logger.getLogger(AvatarService.class.getName());

    @Value("${students.avatar.dir.path}")
    private  String upload;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }



    public Avatar save(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked save");
        String filePath = upload + studentId + "_" + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath);
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        Student student = new Student();
        student.setId(studentId);
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public byte[] getAvatarFromDb(Long studentId) {
        logger.info("Was invoked method for get avatar from DB");
        Avatar avatar = avatarRepository.findByStudentId(studentId);
        return avatar != null ? avatar.getData() : null;
    }

    public byte[] getAvatarFromDisk(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar from Disk");
        Avatar avatar = avatarRepository.findByStudentId(studentId);
        if (avatar == null) return null;

        Path path = Paths.get(avatar.getFilePath());
        return Files.readAllBytes(path);
    }

    public List<Avatar> getAllAvatars(Integer page, Integer size) {
        logger.info("Was invoked method for get all avatars");
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }

}
