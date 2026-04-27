package com.bigdata.second.controller;

import com.bigdata.second.DTO.BorrowRequest;
import com.bigdata.second.entity.Borrower;
import com.bigdata.second.service.BorrowerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService service;

    public BorrowerController(BorrowerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Borrower> borrow(@Valid @RequestBody BorrowRequest request) {
        Borrower borrower = service.borrowBook(
                request.getMemberId(),
                request.getBookId(),
                request.getIssueDate(),
                request.getDueDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(borrower);
    }

    @GetMapping
    public ResponseEntity<List<Borrower>> getAll() {
        return ResponseEntity.ok(service.getAllBorrowers());
    }

    // Yeni eklendi: kitabı iade etme endpoint'i
    @PatchMapping("/{id}/return")
    public ResponseEntity<Borrower> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(service.returnBook(id));
    }
}