package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    private AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<byte[]> uploadAvatar(@PathVariable Long studentId, @RequestParam("file") MultipartFile file) {
        try {
            avatarService.save(studentId, file);
            return ResponseEntity.ok("Аватар загружен успешно".getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(("Ошибка при загрузке аватара: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/db/{studentId}")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        byte[] avatarData = avatarService.getAvatarFromDb(studentId);
        if (avatarData == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg"); // Или другой тип
        return ResponseEntity.ok().headers(headers).body(avatarData);
    }

    @GetMapping("/disk/{studentId}")
    public ResponseEntity<byte[]> getAvatarFromDisk(@PathVariable Long studentId) {
        try {
            byte[] avatarData = avatarService.getAvatarFromDisk(studentId);
            if (avatarData == null) {
                return ResponseEntity.notFound().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpeg"); // Или другой тип
            return ResponseEntity.ok().headers(headers).body(avatarData);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public List<Avatar> getAllAvatars(@RequestParam("page") int page, @RequestParam("size") int size) {
        return avatarService.getAllAvatars(page, size);
    }
}
