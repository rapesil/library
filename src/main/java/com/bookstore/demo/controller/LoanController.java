package com.bookstore.demo.controller;

import com.bookstore.demo.entities.dto.LoanDTO;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.repository.LoanRepository;
import com.bookstore.demo.repository.UserRepository;
import com.bookstore.demo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanDTO loanDTO)  {
        return loanService.createLoan(loanDTO);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        return loanService.returnBook(id);
    }
}
