package com.bookstore.demo.entities;


import com.bookstore.demo.entities.enums.BookStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private Integer pageCount;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

}