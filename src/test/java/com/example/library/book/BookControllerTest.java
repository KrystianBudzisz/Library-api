package com.example.library.book;

import com.example.library.book.model.Book;
import com.example.library.book.model.CreateBookCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    private static Book book;

    @BeforeEach
    public void init() {
        Book bookToSave = Book.builder()
                .id(1L)
                .title("Book Title")
                .author("Book Author")
                .available(true)
                .build();
        book = bookToSave;
        bookRepository.saveAndFlush(bookToSave);
    }


    @Test
    public void shouldCreateBook() throws Exception {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("New Book Title");
        createBookCommand.setAuthor("New Book Author");

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(createBookCommand.getTitle()))
                .andExpect(jsonPath("$.author").value(createBookCommand.getAuthor()))
                .andExpect(jsonPath("$.available").value(true));

        Optional<Book> savedBook = bookRepository.findByTitle(createBookCommand.getTitle());
        assertTrue(savedBook.isPresent());
        assertEquals(createBookCommand.getTitle(), savedBook.get().getTitle());
        assertEquals(createBookCommand.getAuthor(), savedBook.get().getAuthor());
        assertTrue(savedBook.get().isAvailable());
    }


    @Test
    public void shouldBlockBook() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/block")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));

        Optional<Book> blockedBook = bookRepository.findById(book.getId());
        assertTrue(blockedBook.isPresent());
        assertFalse(blockedBook.get().isAvailable());
    }

    @Test
    public void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$.content[0].author", is(book.getAuthor())));

        List<Book> booksInDb = bookRepository.findAll();
        assertEquals(1, booksInDb.size());
    }


    @AfterEach
    void teardown() {
        bookRepository.deleteAll();
    }


}


