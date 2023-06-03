package com.bookstore.demo.mapper;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.dto.BookDTO;
import com.bookstore.demo.entities.enums.BookStatus;

public class BookMapper {

    public static Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.title());
        book.setAuthor(bookDTO.author());
        book.setPublisher(bookDTO.publisher());
        book.setPageCount(bookDTO.pageCount());
        book.setPublicationYear(bookDTO.publicationYear());
        book.setSummary(bookDTO.summary());
        book.setStatus(BookStatus.AVAILABLE);
        return book;
    }

    public static BookDTO toDTO(Book book) {
        return new BookDTO(
            book.getTitle(),
            book.getAuthor(),
            book.getPublisher(),
            book.getPageCount(),
            book.getPublicationYear(),
            book.getSummary()
        );
    }
}
