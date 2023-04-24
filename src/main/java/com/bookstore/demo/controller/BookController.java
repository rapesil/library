package com.bookstore.demo.controller;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService service;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        return service.createABook(bookDTO);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getBookByName(@PathVariable String name) {
        return service.findBookByName(name);
    }

    @PatchMapping("/{id}/lost")
    public ResponseEntity<?> registerLost(@PathVariable Long id) {
        return service.registerLost(id);
    }

    @PatchMapping("/{id}/found")
    public ResponseEntity<?> registerFound(@PathVariable Long id) {
        return service.registerFound(id);
    }

}