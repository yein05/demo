package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Autowired
    @Value("${spring.servlet.multipart.location}")
    private String uploadFolder;

    @GetMapping("/file_upload")
    public String showUploadForm() {
        return "file_upload";
    }

    @PostMapping("/file_upload")
    public String uploadFiles(@RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              RedirectAttributes redirectAttributes) {

        try {
            if (file1.isEmpty() || file2.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "두 개의 파일을 모두 선택해야 합니다.");
                return "/error_page/article_error";
            }

            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            saveFileWithNewName(file1, uploadPath);
            saveFileWithNewName(file2, uploadPath);

            redirectAttributes.addFlashAttribute("message", "파일이 성공적으로 업로드되었습니다.");
            return "upload_end";

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
            return "/error_page/article_error";
        }
    }

    @PostMapping("/upload-email")
    public String uploadEmail(@RequestParam("email") String email,
                              @RequestParam("subject") String subject,
                              @RequestParam("message") String message,
                              RedirectAttributes redirectAttributes) {

        try {
            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            Path filePath = uploadPath.resolve(sanitizedEmail + ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                writer.write("메일 제목: " + subject);
                writer.newLine();
                writer.write("요청 메시지:");
                writer.newLine();
                writer.write(message);
            }
            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");
            return "upload_end";

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
            return "/error_page/article_error";
        }
    }

    private void saveFileWithNewName(MultipartFile file, Path uploadPath) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new IOException("파일 이름이 비어 있습니다.");
        }
        String newName = UUID.randomUUID().toString() + "_" + originalName;
        Path filePath = uploadPath.resolve(newName);
        Files.copy(file.getInputStream(), filePath);
    }
}