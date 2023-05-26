//package com.example.library.book;
//
//import book.BookRepository;
//import book.BookService;
//import book.model.Book;
//import exception.NotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookServiceTest {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @InjectMocks
//    private BookService bookService;
//
//    private Book book;
//
//    @BeforeEach
//    public void setUp() {
//        book = Book.builder()
//                .id(1L)
//                .title("Example Book")
//                .author("John Doe")
//                .available(true)
//                .build();
//    }
//
//    @Test
//    public void testFindById() {
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//
//        Book foundBook = bookService.findById(1L);
//
//        assertEquals(book, foundBook);
//        verify(bookRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testFindById_NotFound() {
//        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> bookService.findById(1L));
//        verify(bookRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testSave() {
//        when(bookRepository.save(any(Book.class))).thenReturn(book);
//
//        Book createdBook = bookService.save(book);
//
//        assertEquals(book, createdBook);
//        verify(bookRepository, times(1)).save(any(Book.class));
//    }
//
//    @Test
//    public void testFindAll() {
//        Pageable pageable = PageRequest.of(0, 5);
//        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), pageable, 1);
//        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
//
//        Page<Book> foundBooks = bookService.findAll(pageable);
//
//        assertEquals(bookPage, foundBooks);
//        verify(bookRepository, times(1)).findAll(pageable);
//    }
//}
//
//
//
