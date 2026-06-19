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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
    //Inject service layer to handle business logic
	@Autowired	
    private BookService svc;

    @PostMapping("/addBook")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO dto) {
        BookDTO addedBook = svc.addBook(dto);
        return ResponseEntity.ok(addedBook);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> findBookByTitle(@PathVariable String title) {
         return ResponseEntity.ok(svc.findBookByTitle(title));
    }

    @GetMapping("/author/{name}")
    public ResponseEntity<List<BookDTO>> findBookByAuthorsName(@PathVariable String name) {
        return ResponseEntity.ok(svc.findBookByAuthorsName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<BookDTO> findBookByAuthorNameAndTitle(@RequestParam String name, @RequestParam String title) {
        return ResponseEntity.ok(svc.findBookByAuthorNameAndTitle(title, name));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        return ResponseEntity.ok(svc.findAllBooks());
    }

    @PutMapping("{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @RequestBody BookDTO dto) {
        BookDTO updated = svc.updateBook(isbn, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn, @RequestParam String name) {
        svc.deleteBook(isbn, name);
        return ResponseEntity.noContent().build();
    }
}