package com.example.library.rental;

import com.example.library.book.BookRepository;
import com.example.library.book.model.Book;
import com.example.library.client.ClientRepository;
import com.example.library.client.model.Client;
import com.example.library.rental.model.CreateRentalCommand;
import com.example.library.rental.model.Rental;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RentalControllerTest {

    private static final Long CLIENT_ID = 1L;
    private static final Long BOOK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        Client client = new Client();
        client.setId(CLIENT_ID);
        client.setFirstName("Alice");
        client.setLastName("Smith");
        clientRepository.saveAndFlush(client);

        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle("Sample Book");
        book.setAvailable(true);
        bookRepository.saveAndFlush(book);
    }

    @Test
    void shouldCreateRental() throws Exception {
        CreateRentalCommand createRentalCommand = CreateRentalCommand.builder()
                .clientId(CLIENT_ID)
                .bookId(BOOK_ID)
                .start(LocalDate.now())
                .end(LocalDate.now().plusDays(7))
                .build();

        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRentalCommand)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(jsonPath("$.bookId").value(BOOK_ID))
                .andExpect(jsonPath("$.start").value(createRentalCommand.getStart().toString()))
                .andExpect(jsonPath("$.end").value(createRentalCommand.getEnd().toString()))
                .andExpect(jsonPath("$.returned").value(false));
    }

    @Test
    void shouldReturnRental() throws Exception {
        Rental rental = createRental();

        mockMvc.perform(put("/api/rentals/{id}/return", rental.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rental.getId()))
                .andExpect(jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(jsonPath("$.bookId").value(BOOK_ID))
                .andExpect(jsonPath("$.start").value(rental.getStart().toString()))
                .andExpect(jsonPath("$.end").value(rental.getEnd().toString()))
                .andExpect(jsonPath("$.returned").value(true));
    }

    @Test
    void shouldGetClientRentals() throws Exception {
        rentalRepository.deleteAll();

        Rental rental = createRental();

        mockMvc.perform(get("/api/rentals/client/{clientId}", CLIENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(rental.getId()))
                .andExpect(jsonPath("$[0].clientId").value(CLIENT_ID))
                .andExpect(jsonPath("$[0].bookId").value(BOOK_ID))
                .andExpect(jsonPath("$[0].start").value(rental.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(rental.getEnd().toString()))
                .andExpect(jsonPath("$[0].returned").value(false));
    }


    private Rental createRental() {
        Rental rental = new Rental();
        rental.setClient(clientRepository.getById(CLIENT_ID));
        rental.setBook(bookRepository.getById(BOOK_ID));
        rental.setStart(LocalDate.now());
        rental.setEnd(LocalDate.now().plusDays(7));
        rental.setReturned(false);
        return rentalRepository.saveAndFlush(rental);
    }

    @AfterEach
    void teardown() {
        rentalRepository.deleteAll();
        clientRepository.deleteAll();
        bookRepository.deleteAll();
    }
}
