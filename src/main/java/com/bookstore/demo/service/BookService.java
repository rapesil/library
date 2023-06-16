package com.bookstore.demo.service;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.enums.BookStatus;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.exceptions.BookNotFoundException;
import com.bookstore.demo.controller.response.ErrorResponse;
import com.bookstore.demo.exceptions.BookNotLostException;
import com.bookstore.demo.exceptions.LossRecordExistsException;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<?> createABook(BookDTO bookDTO) {
        Optional<Book> foundBook = bookRepository.findByTitleIgnoreCase(bookDTO.title());


        if(foundBook.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("This book already exists"));
        }

        Book book = BookMapper.toEntity(bookDTO);

        Book savedBook = bookRepository.save(book);
        BookDTO savedBookDTO = BookMapper.toDTO(savedBook);

        return ResponseEntity.created(URI.create("/books/" + savedBook.getId())).body(savedBookDTO);
    }

    public List<Book> searchBooks(String title, String author, String publisher, Integer year) {
            List<Book> allBooks = bookRepository.findAll();
            List<Book> filteredBooks = new ArrayList<>();

            for (Book book : allBooks) {
                boolean match = true;

                if (title != null && !book.getTitle().equalsIgnoreCase(title)) {
                    match = false;
                }

                if (author != null && !book.getAuthor().equalsIgnoreCase(author)) {
                    match = false;
                }

                if (publisher != null && !book.getPublisher().equalsIgnoreCase(publisher)) {
                    match = false;
                }

                if (year != null && !book.getPublicationYear().equals(year)) {
                    match = false;
                }

                if (match) {
                    filteredBooks.add(book);
                }
            }

            return filteredBooks;
    }

    public Optional<Book> findBookByName(String name) {
        return bookRepository.findByTitleIgnoreCase(name);
    }

    public Book registerLost(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Not Found"));

        BookStatus currentStatus = book.getStatus();
        switch (currentStatus) {
            case AVAILABLE:
                book.setStatus(BookStatus.INTERNAL_LOST);
                break;
            case BORROWED:
                book.setStatus(BookStatus.LOST_BY_CUSTOMER);
                break;
            case LOST_BY_CUSTOMER:
            case INTERNAL_LOST:
                throw new LossRecordExistsException("Loss record already saved");
        }
        return bookRepository.save(book);
    }

    public Book registerFound(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Not Found"));

        BookStatus currentStatus = book.getStatus();
        switch (currentStatus) {
            case INTERNAL_LOST:
                book.setStatus(BookStatus.AVAILABLE);
                break;
            case LOST_BY_CUSTOMER:
                book.setStatus(BookStatus.WAITING_PAYMENT);
                break;
            case AVAILABLE:
            case BORROWED:
                throw new BookNotLostException("Book was not lost. Actual status is " + book.getStatus());
        }
        return bookRepository.save(book);
    }
}
