package com.example.devchallengeimageuploaderapi.model;

import java.util.Optional;

public class ResponseMessage {

    private final String message;
    private final Optional<String> fileLocation;

    public ResponseMessage(String message, Optional<String> fileLocation) {
        this.message = message;
        this.fileLocation = fileLocation;
    }

    public String getMessage() {
        return message;
    }
    
    public Optional<String> getFileLocation() {
        return fileLocation;
    }

}
