package com.bookstore.api.exception;

public class BookTitleAndAuthorNameNotFoundException extends RuntimeException {
    public BookTitleAndAuthorNameNotFoundException(String name, String title) {
        super("Book with author " + name + " and title " + title + " not found");
    }
}