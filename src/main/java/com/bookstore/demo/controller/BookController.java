package com.bookstore.demo.controller;

import com.bookstore.demo.controller.response.BookFoundResponse;
import com.bookstore.demo.controller.response.ErrorResponse;
import com.bookstore.demo.controller.response.LossResponse;
import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.entities.enums.BookStatus;
import com.bookstore.demo.exceptions.BookNotFoundException;
import com.bookstore.demo.exceptions.LossRecordExistsException;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

        Optional<Book> book = service.findBookByName(name);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book Not Found"));
    }

    @PatchMapping("/{id}/lost")
    public ResponseEntity<?> registerLost(@PathVariable Long id) {
        try {
            Book book = service.registerLost(id);

            String message = (book.getStatus() == BookStatus.INTERNAL_LOST)
                    ? "Loss record saved successfully."
                    : "The value for the loss is R$100.00. Please, regularize your situation.";

            LossResponse response = new LossResponse(message, book.getStatus());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BookNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (LossRecordExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PatchMapping("/{id}/found")
    public ResponseEntity<?> registerFound(@PathVariable Long id) {
        try{
            Book book = service.registerFound(id);
            BookFoundResponse response =
                    new BookFoundResponse("Found recorded successfully", book.getStatus());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BookNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        }
    }

}