package com.example.devchallengeimageuploaderapi.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.devchallengeimageuploaderapi.model.ResponseMessage;
import com.example.devchallengeimageuploaderapi.service.ImageService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;
    
    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        // Create the resource object for the image file
        String filePath = "savedImages" + File.separator + imageName;
        Resource resource = new FileSystemResource(filePath);

        // Check if the image file exists
        if (resource.exists()) {
            // Build the response entity with the image content
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .contentType(MediaType.IMAGE_PNG) // Modify according to your image type
                    .body(resource);
        } else {
            // Return a 404 Not Found response if the image file doesn't exist
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> uploadPhoto(@RequestParam("file") MultipartFile file) {

        try {

            String filename = this.imageService.saveImage(file);
            String fileLocation = "http://localhost:8080/api/image/" + filename;

            return new ResponseEntity<>(new ResponseMessage("Uploaded '" + file.getOriginalFilename() + "' successfully",
                                                            Optional.of(fileLocation)),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseMessage("Could not upload '" + file.getOriginalFilename() + "'", Optional.empty()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
