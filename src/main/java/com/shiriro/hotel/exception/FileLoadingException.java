package com.shiriro.hotel.exception;

public class FileLoadingException extends RuntimeException {

    public FileLoadingException(String filePath) {
        super("Could not load file from classpath: " + filePath);
    }

    public FileLoadingException(String filePath, Throwable cause) {
        super("Could not load file from classpath: " + filePath, cause);
    }
}