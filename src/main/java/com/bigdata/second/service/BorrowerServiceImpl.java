package com.bigdata.second.service;

import com.bigdata.second.entity.Book;
import com.bigdata.second.entity.Borrower;
import com.bigdata.second.entity.Member;
import com.bigdata.second.exception.ResourceNotFoundException;
import com.bigdata.second.exception.ValidationException;
import com.bigdata.second.repository.BookRepository;
import com.bigdata.second.repository.BorrowerRepository;
import com.bigdata.second.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    // Bir üyenin aynı anda alabileceği maksimum kitap sayısı
    private static final int MAX_BORROW_LIMIT = 3;

    private final BorrowerRepository borrowerRepo;
    private final MemberRepository memberRepo;
    private final BookRepository bookRepo;

    public BorrowerServiceImpl(BorrowerRepository borrowerRepo,
                               MemberRepository memberRepo,
                               BookRepository bookRepo) {
        this.borrowerRepo = borrowerRepo;
        this.memberRepo = memberRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public Borrower borrowBook(Long memberId, Long bookId,
                               LocalDate issueDate, LocalDate dueDate) {

        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));

        // 1. Tarih kontrolü: due date, issue date'den sonra olmalı
        if (!dueDate.isAfter(issueDate)) {
            throw new ValidationException("İade tarihi, veriliş tarihinden sonra olmalıdır.");
        }

        // 2. Tarih kontrolü: issue date geleceğe ait olmamalı
        if (issueDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Veriliş tarihi gelecekte olamaz.");
        }

        // 3. Kitap müsaitlik kontrolü: kitap zaten başka birinde ödünçte mi?
        if (borrowerRepo.existsByBookIdAndReturnDateIsNull(bookId)) {
            throw new ValidationException(
                    "\"" + book.getTitle() + "\" adlı kitap şu an başka bir üyede ödünçte. " +
                            "İade edilmeden başkasına verilemez."
            );
        }

        // 4. Üye limit kontrolü: üyenin aktif ödünç sayısı limite ulaştı mı?
        long aktifOduncSayisi = borrowerRepo.countByMemberIdAndReturnDateIsNull(memberId);
        if (aktifOduncSayisi >= MAX_BORROW_LIMIT) {
            throw new ValidationException(
                    member.getName() + " adlı üye zaten " + MAX_BORROW_LIMIT + " kitap ödünç almış. " +
                            "Yeni kitap alabilmesi için önce mevcut kitaplardan birini iade etmesi gerekiyor."
            );
        }

        Borrower borrower = new Borrower();
        borrower.setMember(member);
        borrower.setBook(book);
        borrower.setIssueDate(issueDate);
        borrower.setDueDate(dueDate);

        return borrowerRepo.save(borrower);
    }

    @Override
    public List<Borrower> getAllBorrowers() {
        return borrowerRepo.findAll();
    }

    @Override
    public Borrower returnBook(Long borrowerId) {
        Borrower borrower = borrowerRepo.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower", borrowerId));

        if (borrower.getReturnDate() != null) {
            throw new ValidationException(
                    "Bu kitap zaten " + borrower.getReturnDate() + " tarihinde iade edilmiş."
            );
        }

        borrower.setReturnDate(LocalDate.now());
        return borrowerRepo.save(borrower);
    }
}