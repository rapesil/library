package com.bookstore.demo.repository;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatus(BookStatus status);

    Optional<Book> findByTitleIgnoreCase(String title);

    Optional<Book> findByIdAndStatus(Long id, BookStatus status);

}