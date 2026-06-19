package com.bookstore.api.exception;

public class AuthorNameNotFoundException extends RuntimeException {
    public AuthorNameNotFoundException(String name) {
        super("Author with name " + name + " not found");
    }
}
