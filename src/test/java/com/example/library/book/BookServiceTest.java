package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.CreateBookCommand;
import com.example.library.exception.DatabaseException;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
    void testBlockBook() {
        Long bookId = 1L;
        when(bookRepository.blockBook(bookId)).thenReturn(1);

        bookService.blockBook(bookId);

        Mockito.verify(bookRepository, Mockito.times(1)).blockBook(bookId);
    }

    @Test
    void testBlockBookWithFailedBlocking() {
        Long bookId = 1L;
        when(bookRepository.blockBook(bookId)).thenReturn(0);


        assertThrows(ResourceNotFoundException.class, () -> bookService.blockBook(bookId));
    }

    @Test
    void testBlockBookWhenNotExisting() {
        Long nonExistentBookId = 2L;

        when(bookRepository.blockBook(nonExistentBookId)).thenReturn(0);
        assertThrows(ResourceNotFoundException.class, () -> bookService.blockBook(nonExistentBookId));

        Mockito.verify(bookRepository, Mockito.times(1)).blockBook(nonExistentBookId);
    }

    @Test
    public void testBlockBookWhenNotFound() {
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.blockBook(1L));
    }

    @Test
    public void testCreateBookWithEmptyTitle() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("");
        createBookCommand.setAuthor("Author One");

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookWithEmptyAuthor() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("");

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookWithEmptyTitleAndAuthor() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("");
        createBookCommand.setAuthor("");

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookWithNullTitleAndAuthor() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle(null);
        createBookCommand.setAuthor(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookWithUnknownDatabaseError() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("");

        when(bookRepository.save(any(Book.class))).thenThrow(dataIntegrityViolationException);

        assertThrows(DatabaseException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookWithOptimisticLockException() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        when(bookRepository.save(any(Book.class))).thenThrow(new OptimisticLockException());

        assertThrows(ConcurrentModificationException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookWithExistingTitle() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Existing Title");
        createBookCommand.setAuthor("Author One");

        when(bookRepository.existsByTitle(createBookCommand.getTitle())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> bookService.createBook(createBookCommand));
    }

    @Test
    public void testCreateBookMappingLogic() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("New Book");
        createBookCommand.setAuthor("New Author");

        Book book = new Book();
        book.setId(10L);
        book.setTitle("New Book");
        book.setAuthor("New Author");
        book.setAvailable(true);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto resultDto = bookService.createBook(createBookCommand);

        Assertions.assertEquals(book.getId(), resultDto.getId());
        Assertions.assertEquals(book.getTitle(), resultDto.getTitle());
        Assertions.assertEquals(book.getAuthor(), resultDto.getAuthor());
        Assertions.assertEquals(book.isAvailable(), resultDto.isAvailable());
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

    @Test
    void testGetAllBooksWithNoBooks() {
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<BookDto> bookDtoPage = bookService.getAllBooks(PageRequest.of(0, 10));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(any(Pageable.class));
        Assertions.assertTrue(bookDtoPage.getContent().isEmpty());
    }

    @Test
    void testGetAllBooksWithLessBooksThanPageSize() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setAvailable(true);
        books.add(book1);

        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<BookDto> bookDtoPage = bookService.getAllBooks(PageRequest.of(0, 10));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(any(Pageable.class));
        Assertions.assertEquals(1, bookDtoPage.getContent().size());
    }


}

