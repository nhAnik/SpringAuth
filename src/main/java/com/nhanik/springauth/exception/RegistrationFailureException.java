package com.nhanik.springauth.exception;

public class RegistrationFailureException extends RuntimeException {

    public RegistrationFailureException(String email) {
        super("User with email " + email + " already exists");
    }
}
