package com.maddy.studentapi.exception;

/** Thrown when an email is already used by another student. */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
