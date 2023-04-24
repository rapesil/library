package com.bookstore.demo.service;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.BookStatus;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.exceptions.BookAlreadyRegistered;
import com.bookstore.demo.exceptions.ErrorResponse;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<?> createABook(BookDTO bookDTO) {
        var foundBook = bookRepository.findByTitleIgnoreCase(bookDTO.getTitle());


        if(foundBook.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("This book already exists"));
        }

        Book book = BookMapper.toEntity(bookDTO);

        Book savedBook = bookRepository.save(book);
        BookDTO savedBookDTO = BookMapper.toDTO(savedBook);

        return ResponseEntity.created(URI.create("/books/" + savedBook.getId())).body(savedBookDTO);
    }

    public ResponseEntity<?> findBookByName(String name) {
        var book = bookRepository.findByTitleIgnoreCase(name);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book Not Found"));
    }

    public ResponseEntity<?> registerLost(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            if(book.get().getStatus() == BookStatus.BORROWED) {
                book.get().setStatus(BookStatus.LOST_BY_CUSTOMER);
                bookRepository.save(book.get());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ErrorResponse("The value for the loss is R$100.00. Please, regularize your situation."));
            }
            book.get().setStatus(BookStatus.INTERNAL_LOST);
            bookRepository.save(book.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse("Loss record saved successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book Not Found"));
    }

    public ResponseEntity<?> registerFound(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            if(book.get().getStatus() == BookStatus.INTERNAL_LOST) {
                book.get().setStatus(BookStatus.AVAILABLE);
                bookRepository.save(book.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse("Book is now available"));
            }

            if(book.get().getStatus() == BookStatus.LOST_BY_CUSTOMER) {
                book.get().setStatus(BookStatus.WAITING_PAYMENT);
                bookRepository.save(book.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse("Waiting Payment"));
            }

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Book was not lost. Actual status is " + book.get().getStatus()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book Not Found"));
    }
}
