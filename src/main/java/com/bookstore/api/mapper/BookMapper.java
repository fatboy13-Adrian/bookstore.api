package com.bookstore.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.entity.Author;
import com.bookstore.api.entity.Book;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    public BookDTO toDTO(Book b) {
        List<AuthorDTO> authorDTOs = b.getAuthors()
        .stream()
        .map(authorMapper::toDTO)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        BookDTO dto = new BookDTO();
        dto.setIsbn(b.getIsbn());
        dto.setTitle(b.getTitle());
        dto.setYear(b.getYear());
        dto.setPrice(b.getPrice());
        dto.setGenre(b.getGenre());
        dto.setAuthors(authorDTOs);

        return dto;
    }

    public Book toEntity(BookDTO dto) {
        List<Author> authors = dto.getAuthors()
        .stream()
        .map(authorMapper::toEntity)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        return Book.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .year(dto.getYear())
                .price(dto.getPrice())
                .genre(dto.getGenre())
                .authors(authors)
                .build();
    }
}