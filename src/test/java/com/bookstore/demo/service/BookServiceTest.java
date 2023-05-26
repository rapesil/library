package com.bookstore.demo.service;


import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService service;

    @Mock
    private BookRepository mockRepository;

    @Test
    @DisplayName("Deve lançar um erro de conflito quando tentar criar um novo com nome que já existe")
    void createABook_whenBookExist_thenThrow409() {
        Mockito.when(mockRepository.findByTitleIgnoreCase(Mockito.any()))
                .thenReturn(Optional.of(new Book()));

        ResponseEntity<?> response = service.createABook(new BookDTO());

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("Deve cadastrar um livro com sucesso")
    void createABook_succesfully() {
        Mockito.when(mockRepository.findByTitleIgnoreCase(Mockito.any()))
                .thenReturn(Optional.empty());

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Livro 1");
        book.setAuthor("Rafael Peixoto");
        book.setPublisher("Casa do Código");
        book.setPageCount(300);
        book.setPublicationYear(2017);
        book.setSummary("Summary");

        Mockito.when(mockRepository.save(Mockito.any()))
                .thenReturn(new Book());

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Livro 1");
        bookDTO.setAuthor("Rafael Peixoto");
        bookDTO.setPublisher("Casa do Código");
        bookDTO.setPageCount(300);
        bookDTO.setPublicationYear(2017);
        bookDTO.setSummary("Summary");
        ResponseEntity<?> response = service.createABook(bookDTO);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
    }



}