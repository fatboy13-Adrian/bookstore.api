package com.bookstore.api.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.api.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByAuthorsName(String name);
    Optional <Book> findByTitle(String title);
    Optional <Book> findByTitleAndAuthorsName(String title, String name);
}