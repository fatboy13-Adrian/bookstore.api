package com.bookstore.api.mapper;

import org.springframework.stereotype.Component;

import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.entity.Author;

@Component
public class AuthorMapper {
    public AuthorDTO toDTO(Author a) {
        return new AuthorDTO(
            a.getId(),
            a.getName(),
            a.getBirthday()
        );
    }

    public Author toEntity(AuthorDTO dto) {
        return Author.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .birthday(dto.getBirthday())
                    .build();
    }
}