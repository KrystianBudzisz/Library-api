package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.BookMapper;
import com.example.library.book.model.CreateBookCommand;
import com.example.library.exception.DatabaseException;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ConcurrentModificationException;


@AllArgsConstructor
@Service
public class BookService {


    private BookRepository bookRepository;

    private BookMapper bookMapper;

    @Transactional
    public BookDto createBook(CreateBookCommand createBookCommand) {

        if (createBookCommand.getTitle() == null || createBookCommand.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (createBookCommand.getAuthor() == null || createBookCommand.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }

        if (bookRepository.existsByTitle(createBookCommand.getTitle())) {
            throw new DuplicateResourceException("Book with title '" + createBookCommand.getTitle() + "' already exists");
        }

        Book book = new Book();
        book.setTitle(createBookCommand.getTitle());
        book.setAuthor(createBookCommand.getAuthor());
        book.setAvailable(true);

        try {
            book = bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Error occurred while creating the book");
        } catch (OptimisticLockException ole) {
            throw new ConcurrentModificationException("The book was modified by another transaction. Please try again.");
        }

        return bookMapper.mapToDto(book);
    }


    @Transactional
    public void blockBook(Long id) {
        if (bookRepository.blockBook(id) == 0) throw new ResourceNotFoundException("Book", "id", id);
    }


    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::mapToDto);
    }
}
