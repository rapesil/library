package com.bookstore.demo.service;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.enums.BookStatus;
import com.bookstore.demo.entities.Loan;
import com.bookstore.demo.entities.enums.UserStatus;
import com.bookstore.demo.entities.dto.LoanCreated;
import com.bookstore.demo.entities.dto.LoanDTO;
import com.bookstore.demo.controller.response.ErrorResponse;
import com.bookstore.demo.repository.BookRepository;
import com.bookstore.demo.repository.LoanRepository;
import com.bookstore.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoanService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    public LoanService(BookRepository bookRepository, UserRepository userRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    public ResponseEntity<?> createLoan(LoanDTO loanDTO) {
        return bookRepository.findById(loanDTO.getBookId())
                .flatMap(book -> userRepository.findById(loanDTO.getUserId())
                        .map(user -> {
                            if (book.getStatus() == BookStatus.AVAILABLE && user.getStatus() == UserStatus.APPROVED) {
                                book.setStatus(BookStatus.BORROWED);
                                Loan loan = new Loan();
                                loan.setBook(book);
                                loan.setUser(user);
                                loan.setLoanDate(LocalDate.now());
                                loan.setDeadLine(LocalDate.now().plusDays(7L));
                                loanRepository.save(loan);
                                bookRepository.save(book);
                                return ResponseEntity.created(URI.create("/books/" + loan.getId()))
                                        .body(LoanCreated.from(loan));
                            } else {
                                return ResponseEntity.status(HttpStatus.CONFLICT)
                                        .body(new ErrorResponse("Book is not available"));
                            }
                        }))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Book or user not found")));
    }

    public ResponseEntity<?> returnBook(Long id) {
        Optional<Loan> loanOptional = loanRepository.findById(id);

        if (loanOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Loan not found"));
        }

        Loan loan = loanOptional.get();
        Book book = loan.getBook();

        if (book.getStatus() == BookStatus.AVAILABLE) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Book already available"));
        }

        if (LocalDate.now().isAfter(loan.getDeadLine())) {
            if (book.getStatus() == BookStatus.PAYMENT_OK) {
                book.setStatus(BookStatus.AVAILABLE);
                loan.setReturnDate(LocalDate.now());
                loanRepository.save(loan);
                bookRepository.save(book);

                return ResponseEntity.ok(new ErrorResponse("Loan successfully closed."));
            }
            book.setStatus(BookStatus.WAITING_PAYMENT);
            bookRepository.save(book);
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse("Waiting Payment"));
        }
        book.setStatus(BookStatus.AVAILABLE);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);
        bookRepository.save(book);
        return ResponseEntity.ok(new ErrorResponse("Loan successfully closed."));

    }
}




