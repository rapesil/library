package com.bookstore.demo.entities.dto;

public record BookDTO(String title,
                      String author,
                      String publisher,
                      Integer pageCount,
                      Integer publicationYear,
                      String summary) {

  public BookDTO() {
    this(null, null, null, 0, 0, null);
  }
}
