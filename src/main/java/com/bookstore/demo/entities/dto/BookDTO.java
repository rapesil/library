package com.bookstore.demo.entities.dto;

import lombok.Data;

@Data
public class BookDTO {
    private String title;
    private String author;
    private String publisher;
    private Integer pageCount;
    private Integer publicationYear;
    private String summary;

}
