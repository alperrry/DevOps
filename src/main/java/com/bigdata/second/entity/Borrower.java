package com.bigdata.second.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "borrower")
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = true → kitap silinirse book_id NULL olur, kayıt kaybolmaz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true,
            foreignKey = @ForeignKey(
                    name = "FK_borrower_member",
                    foreignKeyDefinition = "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE SET NULL"
            ))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = true,
            foreignKey = @ForeignKey(
                    name = "FK_borrower_book",
                    foreignKeyDefinition = "FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL"
            ))
    private Book book;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
