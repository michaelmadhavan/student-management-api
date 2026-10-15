package com.maddy.studentapi.exception;

/** Thrown when a student id does not exist. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
