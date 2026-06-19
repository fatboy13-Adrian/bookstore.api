package com.bookstore.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.entity.Author;
import com.bookstore.api.exception.AuthorNameNotFoundException;
import com.bookstore.api.exception.AuthorNotFoundException;
import com.bookstore.api.mapper.AuthorMapper;
import com.bookstore.api.repository.AuthorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {
    //Repository for CRUD operation
    private final AuthorRepository authorRepository;

    //Mapper class for conversion between entity / dto
    private final AuthorMapper authorMapper;

    //Create new author and save record into DB
    @Transactional
    public AuthorDTO addAuthor(AuthorDTO dto) {
        //Convert DTO to entity
        Author a = authorMapper.toEntity(dto);

        //Save to DB
        Author saved = authorRepository.save(a);
        
        //Convert back to DTO
        return authorMapper.toDTO(saved);
    }

    //Find author by ID
    public AuthorDTO findAuthorById(Long id) {
        //Throw exception if not found
        Author a = authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
        
        //Convert to DTO
        return authorMapper.toDTO(a);
    }

    //Find author by name
    public AuthorDTO findAuthorByName(String name) {
        Author a = authorRepository.findByName(name).orElseThrow(() -> new AuthorNameNotFoundException(name));
        return authorMapper.toDTO(a);
    }

    //Find all authors
    public List <AuthorDTO> findAllAuthors() {
        return authorRepository.findAll()
        .stream().map(authorMapper::toDTO)
        .toList();
    }

    //Update existing record
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO dto) {
        //Ensure record exists
        Author existing = authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
        
        //Update fields
        existing.setName(dto.getName());
        existing.setBirthday(dto.getBirthday());
        
        //Save updated fields into DB and return DTO
        Author updated = authorRepository.save(existing);
        return authorMapper.toDTO(updated);
    }

    //Delete record by ID
    public void deleteAuthor(Long id) {
        //Ensure record exists
        if (!authorRepository.existsById(id))
            throw new AuthorNotFoundException(id);

        //Delete from DB
        authorRepository.deleteById(id);
    }
}