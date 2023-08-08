package com.example.devchallengeimageuploaderapi.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    public String saveImage(MultipartFile file) throws IllegalStateException, IOException {

        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
        String randomFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = "savedImages" + File.separator + randomFileName;
        Path destFile = Paths.get(filePath).toAbsolutePath().normalize();
        file.transferTo(destFile);

        return randomFileName;
    }

}
