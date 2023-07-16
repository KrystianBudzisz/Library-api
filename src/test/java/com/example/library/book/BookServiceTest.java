package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.CreateBookCommand;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;
    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Test
    void testCreateBook() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setAvailable(true);

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setTitle("Book One");
        expectedBookDto.setAuthor("Author One");
        expectedBookDto.setAvailable(true);

        when(bookRepository.save(bookCaptor.capture())).thenReturn(book);

        BookDto createdBookDto = bookService.createBook(createBookCommand);

        Mockito.verify(bookRepository, Mockito.times(1)).save(bookCaptor.getValue());

        Book savedBook = bookCaptor.getValue();
        Assertions.assertEquals("Book One", savedBook.getTitle());
        Assertions.assertEquals("Author One", savedBook.getAuthor());

        Assertions.assertEquals(expectedBookDto.getId(), createdBookDto.getId());
        Assertions.assertEquals(expectedBookDto.getTitle(), createdBookDto.getTitle());
        Assertions.assertEquals(expectedBookDto.getAuthor(), createdBookDto.getAuthor());
        Assertions.assertEquals(expectedBookDto.isAvailable(), createdBookDto.isAvailable());
    }

    @Test
    public void testCreateBookWhenDuplicate() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        Book existingBook = new Book();
        existingBook.setTitle("Book One");
        existingBook.setAuthor("Author One");
        existingBook.setAvailable(true);

        when(bookRepository.findByTitle(any(String.class))).thenReturn(Optional.of(existingBook));

        assertThrows(DuplicateResourceException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    void testBlockBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setAvailable(true);

        BookDto expectedBlockedBookDto = new BookDto();
        expectedBlockedBookDto.setId(1L);
        expectedBlockedBookDto.setTitle("Book One");
        expectedBlockedBookDto.setAuthor("Author One");
        expectedBlockedBookDto.setAvailable(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(bookCaptor.capture())).thenReturn(book);

        BookDto blockedBookDto = bookService.blockBook(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).save(bookCaptor.getValue());

        Book savedBook = bookCaptor.getValue();
        Assertions.assertFalse(savedBook.isAvailable());

        Assertions.assertEquals(expectedBlockedBookDto.getId(), blockedBookDto.getId());
        Assertions.assertEquals(expectedBlockedBookDto.getTitle(), blockedBookDto.getTitle());
        Assertions.assertEquals(expectedBlockedBookDto.getAuthor(), blockedBookDto.getAuthor());
        Assertions.assertEquals(expectedBlockedBookDto.isAvailable(), blockedBookDto.isAvailable());
    }

    @Test
    public void testBlockBookWhenNotFound() {
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.blockBook(1L));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setAvailable(true);
        books.add(book1);
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book Two");
        book2.setAuthor("Author Two");
        book2.setAvailable(true);
        books.add(book2);

        List<BookDto> expectedBookDtos = books.stream().map(book -> {
            BookDto bookDto = new BookDto();
            bookDto.setId(book.getId());
            bookDto.setTitle(book.getTitle());
            bookDto.setAuthor(book.getAuthor());
            bookDto.setAvailable(book.isAvailable());
            return bookDto;
        }).toList();

        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<BookDto> bookDtoPage = bookService.getAllBooks(PageRequest.of(0, 10));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(any(Pageable.class));

        Assertions.assertEquals(expectedBookDtos.size(), bookDtoPage.getContent().size());
        for (int i = 0; i < expectedBookDtos.size(); i++) {
            BookDto expectedBookDto = expectedBookDtos.get(i);
            BookDto actualBookDto = bookDtoPage.getContent().get(i);
            Assertions.assertEquals(expectedBookDto.getId(), actualBookDto.getId());
            Assertions.assertEquals(expectedBookDto.getTitle(), actualBookDto.getTitle());
            Assertions.assertEquals(expectedBookDto.getAuthor(), actualBookDto.getAuthor());
            Assertions.assertEquals(expectedBookDto.isAvailable(), actualBookDto.isAvailable());
        }
    }

}

