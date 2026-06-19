package com.bookstore.api.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.service.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    //Inject service layer to handle business logic
	@Autowired	
	private AuthorService svc;

    @PostMapping("/addAuthor")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO dto) {
        AuthorDTO addedAuthor = svc.addAuthor(dto);
        return ResponseEntity.ok(addedAuthor);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AuthorDTO> findAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findAuthorById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AuthorDTO> findAuthorByName(@PathVariable String name) {
        return ResponseEntity.ok(svc.findAuthorByName(name));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAllAuthors() {
        return ResponseEntity.ok(svc.findAllAuthors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO dto) {
        return ResponseEntity.ok(svc.updateAuthor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        svc.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
