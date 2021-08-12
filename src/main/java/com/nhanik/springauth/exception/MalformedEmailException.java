package com.nhanik.springauth.exception;

public class MalformedEmailException extends RuntimeException {

    public MalformedEmailException(String email) {
        super("Email " + email + " is malformed");
    }
}
