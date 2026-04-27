package com.bigdata.second.service;

import com.bigdata.second.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book createBook(Book book);
    Book findById(Long id);
    Book update(Long id, Book newBook);
    void delete(Long id);
}
