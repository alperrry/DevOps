package com.bigdata.second.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Publisher cannot be blank")
    @Size(max = 255, message = "Publisher must be at most 255 characters")
    private String publisher;

    @NotNull(message = "Publish date cannot be null")
    @PastOrPresent(message = "Publish date cannot be in the future")
    private LocalDate publishDate;
}
