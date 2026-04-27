package com.bigdata.second.service;

import com.bigdata.second.entity.Borrower;

import java.time.LocalDate;
import java.util.List;

public interface BorrowerService {
    Borrower borrowBook(Long memberId, Long bookId,
                        LocalDate issueDate, LocalDate dueDate);
    List<Borrower> getAllBorrowers();

    Borrower returnBook(Long borrowerId);
}
