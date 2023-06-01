package com.bookstore.demo.entities.dto;

import com.bookstore.demo.entities.Loan;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.mapper.UserMapper;

public record LoanCreated(Long id, BookDTO book, UserDTO user) {
    public static LoanCreated from(Loan loan) {
        return new LoanCreated(
            loan.getId(),
            BookMapper.toDTO(loan.getBook()),
            UserMapper.toDTO(loan.getUser())
        );
    }
}