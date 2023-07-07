package com.example.library.book;

import com.example.library.book.model.Book;
import com.example.library.book.model.CreateBookCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setup() {
        book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Book Author");
        book.setAvailable(true);
        book = bookRepository.save(book);
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
    }

    @Test
    public void shouldBlockBook() throws Exception {
        mockMvc.perform(put("/api/books/" + book.getId() + "/block")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    public void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("Book One")))
                .andExpect(jsonPath("$.content[0].author", is("Author One")));
    }
}
