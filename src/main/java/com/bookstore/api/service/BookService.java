package com.bookstore.api.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.entity.Author;
import com.bookstore.api.entity.Book;
import com.bookstore.api.exception.AuthorNameNotFoundException;
import com.bookstore.api.exception.AuthorNotFoundException;
import com.bookstore.api.exception.BookNotFoundException;
import com.bookstore.api.exception.BookTitleAndAuthorNameNotFoundException;
import com.bookstore.api.exception.BookTitleNotFoundException;
import com.bookstore.api.mapper.BookMapper;
import com.bookstore.api.repository.AuthorRepository;
import com.bookstore.api.repository.BookRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    //Repositories for book and author
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    //Mapper class to convert between entity and DTO
    private final BookMapper bookMapper;
    
    //Add new book record into DB
    @Transactional
    public BookDTO addBook(BookDTO dto) {
        //Create new record manually
        Book b = new Book();
        b.setIsbn(dto.getIsbn());
        b.setTitle(dto.getTitle());
        b.setGenre(dto.getGenre());
        b.setYear(dto.getYear());
        b.setPrice(dto.getPrice());

        //Prepare list of managed author entities
        List<Author> authors = new ArrayList<>();

        //Convert author DTO list to managed author entities
        for (AuthorDTO a : dto.getAuthors()) {
            //Fetch author from DB to ensure it is managed by JPA
            Author managedAuthor = authorRepository.findById(a.getId())
            .orElseThrow(() -> new RuntimeException("Author not found: " + a.getId()));
            authors.add(managedAuthor);
        }

        //Set authors for book record
        b.setAuthors(authors);

        //Save book record into DB
        bookRepository.save(b);

        //Convert entity back to DTO
        return bookMapper.toDTO(b);
    }

    //Find book record by its title
    public BookDTO findBookByTitle(String title) {
        //Throw an exception if not found
        Book b = bookRepository.findByTitle(title)
        .orElseThrow(() -> new BookTitleNotFoundException(title));
        return bookMapper.toDTO(b);
    }

    //Find book record by the author's name
    public List<BookDTO> findBookByAuthorsName(String name) {
        List<Book> b = bookRepository.findByAuthorsName(name);
        
        if (b.isEmpty())
            throw new AuthorNameNotFoundException(name);

        //Convert entitiy list to DTO list
        return b.stream()
        .map(bookMapper::toDTO)
        .toList();
    }

    //Find book by title AND author name
    public BookDTO findBookByAuthorNameAndTitle(String title, String name) {
        return bookRepository.findByTitleAndAuthorsName(title, name)
        .map(bookMapper::toDTO)
        .orElseThrow(() -> new BookTitleAndAuthorNameNotFoundException(title, name));
    }

    //Get all books
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    //Update book record and its authors
    @Transactional
    public BookDTO updateBook(String isbn, BookDTO dto) {
        //Find existing book by ID (ISBN)
        Book book = bookRepository.findById(isbn)
        .orElseThrow(() -> new RuntimeException("Book not found"));

        //Update basic fields
        book.setTitle(dto.getTitle());
        book.setGenre(dto.getGenre());
        book.setYear(dto.getYear());
        book.setPrice(dto.getPrice());

        //Convert incoming author DTOs into managed Author entities
        List<Author> authors = dto.getAuthors().stream()
        .map(a -> authorRepository.findById(a.getId())
        .orElseThrow(() -> new AuthorNotFoundException(a.getId())))
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        //Replace existing book-author relationship
        book.setAuthors(authors);

        //Save updated book
        Book updated = bookRepository.save(book);

        //Convert to DTO
        return bookMapper.toDTO(updated);
    }

    //Delete book only if requester matches an author of the book
    public void deleteBook(String isbn, String name) {
        //Find book or throw exception
        Book b = bookRepository.findById(isbn)
        .orElseThrow(() -> new BookNotFoundException(isbn));

        //Check if given name belongs to one of the book's authors
        boolean isAuthor = b.getAuthors()
        .stream().anyMatch(a -> a.getName().equals(name));

        //Reject deletion if user is not an author
        if (!isAuthor)
            throw new RuntimeException("Only author name can delete the book record");

        //Delete book
        bookRepository.delete(b);
    }
}