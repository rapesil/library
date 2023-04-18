package com.bookstore.demo.controller;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.exceptions.BookAlreadyRegistered;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        var foundBook = bookRepository.findByTitleContainingIgnoreCase(bookDTO.getTitle());
        Book book = new Book();

        if(foundBook.isPresent()) {
            throw new BookAlreadyRegistered("Book already registered");
        } else {
            book = BookMapper.toEntity(bookDTO);
        }
        Book savedBook = bookRepository.save(book);
        BookDTO savedBookDTO = BookMapper.toDTO(savedBook);

        return ResponseEntity.created(URI.create("/books/" + savedBook.getId())).body(savedBookDTO);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Book> getBookByName(@PathVariable String name) {
        var book = bookRepository.findByTitleContainingIgnoreCase(name);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}