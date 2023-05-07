package com.example.library.book;

import book.BookRepository;
import book.BookService;
import book.model.Book;
import book.model.BookDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(new Book(1L, "Title 1", "Author 1", false), new Book(2L, "Title 2", "Author 2", false)));
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<Book> books = bookService.findAll(pageable);

        assertEquals(2, books.getNumberOfElements());
        assertEquals("Title 1", books.getContent().get(0).getTitle());
        assertEquals("Author 1", books.getContent().get(0).getAuthor());
    }

    @Test
    void save() {
        Book bookToSave = new Book(null, "Title 1", "Author 1", false);
        Book savedBook = new Book(1L, "Title 1", "Author 1", false);
        when(bookRepository.save(bookToSave)).thenReturn(savedBook);

        Book result = bookService.save(bookToSave);

        assertEquals(1L, result.getId());
        assertEquals("Title 1", result.getTitle());
        assertEquals("Author 1", result.getAuthor());
    }

    @Test
    void findById() {
        Book book = new Book(1L, "Title 1", "Author 1", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.findById(1L);

        assertEquals(1L, foundBook.getId());
        assertEquals("Title 1", foundBook.getTitle());
        assertEquals("Author 1", foundBook.getAuthor());
    }

    @Test
    void block() {
        Book bookToBlock = new Book(1L, "Title 1", "Author 1", false);
        Book blockedBook = new Book(1L, "Title 1", "Author 1", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookToBlock));
        when(bookRepository.save(bookToBlock)).thenReturn(blockedBook);

        Book result = bookService.block(1L);

        assertTrue(result.isBlocked());
    }

    @Test
    void update() {
        Book originalBook = new Book(1L, "Title 1", "Author 1", false);
        BookDto updatedBookDto = new BookDto(1L, "Title 2", "Author 2", true);
        Book updatedBook = new Book(1L, "Title 2", "Author 2", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(originalBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.update(1L, updatedBookDto);

        assertEquals(1L, result.getId());
        assertEquals("Title 2", result.getTitle());
        assertEquals("Author 2", result.getAuthor());
        assertTrue(result.isBlocked());
    }

    @Test
    void delete() {
        Book bookToDelete = new Book(1L, "Title 1", "Author 1", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookToDelete));
        doNothing().when(bookRepository).delete(bookToDelete);

        bookService.delete(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(bookToDelete);
    }
}


