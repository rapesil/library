package com.bookstore.demo.service;

import com.bookstore.demo.entities.Book;
import com.bookstore.demo.entities.BookStatus;
import com.bookstore.demo.entities.Loan;
import com.bookstore.demo.entities.dto.LoanCreated;
import com.bookstore.demo.entities.dto.LoanDTO;
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
                            if (book.getStatus() == BookStatus.AVAILABLE) {
                                book.setStatus(BookStatus.BORROWED);
                                Loan loan = new Loan();
                                loan.setBook(book);
                                loan.setUser(user);
                                loan.setLoanDate(LocalDate.now());
                                loanRepository.save(loan);
                                bookRepository.save(book);
                                return ResponseEntity.created(URI.create("/books/" + loan.getId())).body(LoanCreated.from(loan));
                            } else {
                                return ResponseEntity.status(HttpStatus.CONFLICT).body("Livro não disponível");
                            }
                        }))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro ou usuário não encontrado"));
    }

    public ResponseEntity<String> returnBook(Long id) {
        Optional<Loan> loanOptional = loanRepository.findById(id);

        if (loanOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado");
        }

        Loan loan = loanOptional.get();
        Book book = loan.getBook();

        if (book.getStatus() == BookStatus.AVAILABLE) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Livro já está disponível");
        }

        book.setStatus(BookStatus.AVAILABLE);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);
        bookRepository.save(book);

        return ResponseEntity.ok("Empréstimo encerrado com sucesso");
    }
}




