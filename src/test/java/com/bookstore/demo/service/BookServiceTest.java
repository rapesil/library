package com.bookstore.demo.service;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.entities.enums.BookStatus;
import com.bookstore.demo.exceptions.LossRecordExistsException;
import com.bookstore.demo.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService service;

    @Mock
    private BookRepository mockRepository;

    @Test
    @DisplayName("Deve lançar um erro de conflito quando tentar criar um novo com nome que já existe")
    void createABook_whenBookExist_thenThrow409() {
        when(mockRepository.findByTitleIgnoreCase(any()))
                .thenReturn(Optional.of(new Book()));

        ResponseEntity<?> response = service.createABook(new BookDTO());

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("Deve cadastrar um livro com sucesso")
    void createABook_succesfully() {
        when(mockRepository.findByTitleIgnoreCase(any()))
                .thenReturn(Optional.empty());

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Livro 1");
        book.setAuthor("Rafael Peixoto");
        book.setPublisher("Casa do Código");
        book.setPageCount(300);
        book.setPublicationYear(2017);
        book.setSummary("Summary");

        when(mockRepository.save(any()))
                .thenReturn(new Book());

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Livro 1");
        bookDTO.setAuthor("Rafael Peixoto");
        bookDTO.setPublisher("Casa do Código");
        bookDTO.setPageCount(300);
        bookDTO.setPublicationYear(2017);
        bookDTO.setSummary("Summary");
        ResponseEntity<?> response = service.createABook(bookDTO);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Deve registar perda de um livro por cliente com sucesso")
    void registerLost_byCustomer_successfully() {
        Book book = getBookWithStatus(BookStatus.BORROWED);

        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
        when(mockRepository.save(any(Book.class))).thenReturn(book);

        Book bookRegisteredAsLost = service.registerLost(1L);

        assertThat(bookRegisteredAsLost.getStatus()).isEqualTo(BookStatus.LOST_BY_CUSTOMER);
        verify(mockRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Deve registar perda interna de um livro com sucesso")
    void registerLost_internally_successfully() {
        Book book = getBookWithStatus(BookStatus.AVAILABLE);

        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
        when(mockRepository.save(any(Book.class))).thenReturn(book);

        Book bookRegisteredAsLost = service.registerLost(1L);

        assertThat(bookRegisteredAsLost.getStatus()).isEqualTo(BookStatus.INTERNAL_LOST);
        verify(mockRepository, times(1)).save(book);
    }

    @ParameterizedTest
    @MethodSource("provideStatuses")
    @DisplayName("Deve informar que o livro informado já tem registro de perda")
    void registerLost_whenBookAlreadyRegisteredAsLost_shouldReturnError(BookStatus status) {
        Book book = getBookWithStatus(status);

        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> service.registerLost(1L))
                        .isInstanceOf(LossRecordExistsException.class)
                        .hasMessage("Loss record already saved");
        verify(mockRepository, never()).save(book);
    }

    private static Stream<Arguments> provideStatuses() {
        return Stream.of(
                Arguments.of(BookStatus.LOST_BY_CUSTOMER),
                Arguments.of(BookStatus.INTERNAL_LOST)
        );
    }

    private Book getBookWithStatus(BookStatus status) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Livro 1");
        book.setAuthor("Rafael Peixoto");
        book.setPublisher("Casa do Código");
        book.setPageCount(300);
        book.setPublicationYear(2017);
        book.setSummary("Summary");
        book.setStatus(status);
        return book;
    }

}