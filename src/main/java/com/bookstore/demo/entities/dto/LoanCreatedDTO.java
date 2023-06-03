package com.bookstore.demo.entities.dto;

import com.bookstore.demo.entities.Loan;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.mapper.UserMapper;

public record LoanCreatedDTO(Long id, BookDTO book, UserDTO user) {
    public static LoanCreatedDTO from(Loan loan) {
        return new LoanCreatedDTO(
            loan.getId(),
            BookMapper.toDTO(loan.getBook()),
            UserMapper.toDTO(loan.getUser())
        );
    }
}