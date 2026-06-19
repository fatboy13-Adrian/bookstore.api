package com.bookstore.api.exception;

public class BookTitleNotFoundException extends RuntimeException {
    public BookTitleNotFoundException(String title) {
        super("Book with title " + title + " not found");
    }
}