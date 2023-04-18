package com.bookstore.demo.controller;

import com.bookstore.demo.entities.*;
import com.bookstore.demo.entities.dto.LoanCreated;
import com.bookstore.demo.entities.dto.LoanDTO;
import com.bookstore.demo.mapper.BookMapper;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.repository.LoanRepository;
import com.bookstore.demo.repository.UserRepository;
import com.bookstore.demo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

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
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        Optional<Loan> loanOptional = loanRepository.findById(id);

        if (loanOptional.isPresent()) {
            Loan loan = loanOptional.get();
            Book book = loan.getBook();

            if (book.getStatus() == BookStatus.BORROWED) {
                book.setStatus(BookStatus.AVAILABLE);
                loan.setReturnDate(LocalDate.now());
                loanRepository.save(loan);
                bookRepository.save(book);
                return ResponseEntity.ok("Empréstimo encerrado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Livro já está disponível");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
        }
    }
}
