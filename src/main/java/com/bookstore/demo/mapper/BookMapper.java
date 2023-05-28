package com.bookstore.demo.mapper;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.entities.enums.BookStatus;

public class BookMapper {

    public static Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setPageCount(bookDTO.getPageCount());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setSummary(bookDTO.getSummary());
        book.setStatus(BookStatus.AVAILABLE);
        return book;
    }

    public static BookDTO toDTO(Book book) {
        BookDTO savedBookDTO = new BookDTO();
        savedBookDTO.setTitle(book.getTitle());
        savedBookDTO.setAuthor(book.getAuthor());
        savedBookDTO.setPublisher(book.getPublisher());
        savedBookDTO.setPageCount(book.getPageCount());
        savedBookDTO.setPublicationYear(book.getPublicationYear());
        savedBookDTO.setSummary(book.getSummary());
        return savedBookDTO;
    }
}
