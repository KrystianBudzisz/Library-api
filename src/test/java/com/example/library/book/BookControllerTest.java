package com.example.library.book;

import com.example.library.book.model.BookDto;
import com.example.library.book.model.CreateBookCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    void testCreateBook() throws Exception {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createBookCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book One"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author One"));
    }

    @Test
    void testBlockBook() throws Exception {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        BookDto createdBookDto = bookService.createBook(createBookCommand);

        BookDto blockedBookDto = bookService.blockBook(createdBookDto.getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}/block", blockedBookDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(blockedBookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(blockedBookDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(blockedBookDto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(blockedBookDto.isAvailable()));
    }

    @Test
    void testGetAllBooks() throws Exception {
        CreateBookCommand createBookCommand1 = new CreateBookCommand();
        createBookCommand1.setTitle("Book One");
        createBookCommand1.setAuthor("Author One");

        CreateBookCommand createBookCommand2 = new CreateBookCommand();
        createBookCommand2.setTitle("Book Two");
        createBookCommand2.setAuthor("Author Two");

        bookService.createBook(createBookCommand1);
        bookService.createBook(createBookCommand2);

        Page<BookDto> books = bookService.getAllBooks(PageRequest.of(0, 5));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        Matchers.hasSize(Math.toIntExact(books.getTotalElements()))));
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
