package com.example.library.book;

import com.example.library.book.model.BookDto;
import com.example.library.book.model.CreateBookCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testCreateBook() throws Exception {
        // Utwórz dane wejściowe żądania
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");

        // Utwórz książkę DTO do zwrócenia
        BookDto createdBookDto = new BookDto();
        createdBookDto.setId(1L);
        createdBookDto.setTitle("Book One");
        createdBookDto.setAuthor("Author One");

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(bookService.createBook(createBookCommand)).thenReturn(createdBookDto);

        // Wykonaj żądanie POST na /api/books
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createBookCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdBookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdBookDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(createdBookDto.getAuthor()));

        // Sprawdź, czy metoda createBook() w serwisie została wywołana raz
        Mockito.verify(bookService, Mockito.times(1)).createBook(createBookCommand);
    }

    @Test
    void testBlockBook() throws Exception {
        // Utwórz książkę DTO do zwrócenia
        BookDto blockedBookDto = new BookDto();
        blockedBookDto.setId(1L);
        blockedBookDto.setTitle("Book One");
        blockedBookDto.setAuthor("Author One");
        blockedBookDto.setAvailable(false);

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(bookService.blockBook(1L)).thenReturn(blockedBookDto);

        // Wykonaj żądanie PUT na /api/books/{id}/block
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}/block", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(blockedBookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(blockedBookDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(blockedBookDto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(blockedBookDto.isAvailable()));

        // Sprawdź, czy metoda blockBook() w serwisie została wywołana raz
        Mockito.verify(bookService, Mockito.times(1)).blockBook(1L);
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Utwórz listę książek do zwrócenia
        List<BookDto> bookDtoList = new ArrayList<>();
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book One");
        bookDto1.setAuthor("Author One");
        bookDto1.setAvailable(true);
        bookDtoList.add(bookDto1);
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book Two");
        bookDto2.setAuthor("Author Two");
        bookDto2.setAvailable(true);
        bookDtoList.add(bookDto2);
        Page<BookDto> bookDtoPage = new PageImpl<>(bookDtoList);

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(bookService.getAllBooks(Mockito.any(Pageable.class))).thenReturn(bookDtoPage);

        // Wykonaj żądanie GET na /api/books
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(bookDto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(bookDto1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].author").value(bookDto1.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].available").value(bookDto1.isAvailable()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(bookDto2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].title").value(bookDto2.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].author").value(bookDto2.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].available").value(bookDto2.isAvailable()));

        // Sprawdź, czy metoda getAllBooks() w serwisie została wywołana raz
        Mockito.verify(bookService, Mockito.times(1)).getAllBooks(Mockito.any(Pageable.class));
    }

    // Metoda pomocnicza do zamiany obiektu na JSON
    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
