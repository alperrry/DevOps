package com.bigdata.second.service;

import com.bigdata.second.entity.Book;
import com.bigdata.second.exception.ResourceNotFoundException;
import com.bigdata.second.exception.ValidationException;
import com.bigdata.second.repository.BookRepository;
import com.bigdata.second.repository.BorrowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    @Override
    public Book update(Long id, Book newBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
        book.setTitle(newBook.getTitle());
        book.setPublisher(newBook.getPublisher());
        book.setPublishDate(newBook.getPublishDate());
        return bookRepository.save(book);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        // Aktif ödünç varsa silme
        if (borrowerRepository.existsByBookIdAndReturnDateIsNull(id)) {
            throw new ValidationException(
                    "Bu kitap silinemez çünkü şu an bir üyede ödünçte. Önce kitabı iade alın."
            );
        }
        // Aktif ödünç yok → MySQL ON DELETE SET NULL devreye girer,
        // geçmiş borrower kayıtlarındaki book_id otomatik NULL'a çekilir.
        bookRepository.deleteById(id);
    }
}