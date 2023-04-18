package com.bookstore.demo.entities.dto;

import com.bookstore.demo.entities.Loan;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanCreated {
    private Long id;
    private BookDTO book;
    private UserDTO user;

    public static LoanCreated from(Loan loan) {
        return LoanCreated.builder()
                .id(loan.getId())
                .book(BookMapper.toDTO(loan.getBook()))
                .user(UserMapper.toDTO(loan.getUser()))
                .build();
    }
}