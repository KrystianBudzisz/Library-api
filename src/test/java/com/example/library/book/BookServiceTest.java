package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.CreateBookCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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


@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

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

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookDto createdBookDto = bookService.createBook(createBookCommand);

        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));

        Assertions.assertEquals(expectedBookDto.getId(), createdBookDto.getId());
        Assertions.assertEquals(expectedBookDto.getTitle(), createdBookDto.getTitle());
        Assertions.assertEquals(expectedBookDto.getAuthor(), createdBookDto.getAuthor());
        Assertions.assertEquals(expectedBookDto.isAvailable(), createdBookDto.isAvailable());
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

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookDto blockedBookDto = bookService.blockBook(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));

        Assertions.assertEquals(expectedBlockedBookDto.getId(), blockedBookDto.getId());
        Assertions.assertEquals(expectedBlockedBookDto.getTitle(), blockedBookDto.getTitle());
        Assertions.assertEquals(expectedBlockedBookDto.getAuthor(), blockedBookDto.getAuthor());
        Assertions.assertEquals(expectedBlockedBookDto.isAvailable(), blockedBookDto.isAvailable());
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

        Mockito.when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(bookPage);

        Page<BookDto> bookDtoPage = bookService.getAllBooks(PageRequest.of(0, 10));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));

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

