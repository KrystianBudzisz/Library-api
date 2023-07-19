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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RentalControllerTest {

    private static Rental rental;
    private static Book book;
    private static Client client;

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
        Client clientToSave = Client.builder()
                .firstName("Alice")
                .lastName("Smith")
                .build();
        client = clientRepository.saveAndFlush(clientToSave);

        Book bookToSave = Book.builder()
                .title("Sample Book")
                .author("Ronaldinio")
                .available(true)
                .build();
        book = bookRepository.saveAndFlush(bookToSave);

        Rental rentalToSave = Rental.builder()
                .book(book)
                .client(client)
                .start((LocalDate.now()))
                .end((LocalDate.now().plusDays(7)))
                .returned(false)
                .build();

        rental = rentalRepository.saveAndFlush(rentalToSave);
    }

    @Test
    void shouldCreateRental() throws Exception {
        CreateRentalCommand createRentalCommand = CreateRentalCommand.builder()
                .clientId(client.getId())
                .bookId(book.getId())
                .start(LocalDate.now().plusDays(8))
                .end(LocalDate.now().plusDays(9))
                .build();

        mockMvc.perform(post("/api/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRentalCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientId").value(createRentalCommand.getClientId()))
                .andExpect(jsonPath("$.bookId").value(createRentalCommand.getBookId()))
                .andExpect(jsonPath("$.start").value(createRentalCommand.getStart().toString()))
                .andExpect(jsonPath("$.end").value(createRentalCommand.getEnd().toString()))
                .andExpect(jsonPath("$.returned").value(false));

        Optional<Client> savedClient = clientRepository.findById(client.getId());
        assertTrue(savedClient.isPresent());

        Optional<Book> savedBook = bookRepository.findById(book.getId());
        assertTrue(savedBook.isPresent());

    }

    @Test
    void shouldReturnRental() throws Exception {
        mockMvc.perform(put("/api/rentals/{id}/return", rental.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rental.getId()))
                .andExpect(jsonPath("$.clientId").value(rental.getClientId()))
                .andExpect(jsonPath("$.bookId").value(rental.getBookId()))
                .andExpect(jsonPath("$.start").value(rental.getStart().toString()))
                .andExpect(jsonPath("$.end").value(rental.getEnd().toString()))
                .andExpect(jsonPath("$.returned").value(true));

        Optional<Rental> updatedRental = rentalRepository.findById(rental.getId());
        assertTrue(updatedRental.isPresent());
        assertTrue(updatedRental.get().isReturned());
    }

    @Test
    void shouldGetClientRentals() throws Exception {
        mockMvc.perform(get("/api/rentals/client/{id}/rentals", client.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(rental.getId()))
                .andExpect(jsonPath("$[0].clientId").value(rental.getClientId()))
                .andExpect(jsonPath("$[0].bookId").value(rental.getBookId()))
                .andExpect(jsonPath("$[0].start").value(rental.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(rental.getEnd().toString()))
                .andExpect(jsonPath("$[0].returned").value(false));

        List<Rental> rentalsFromRepo = rentalRepository.findByClientId(client.getId());
        assertEquals(1, rentalsFromRepo.size());
        assertEquals(rental.getId(), rentalsFromRepo.get(0).getId());
    }

    @AfterEach
    void teardown() {
        rentalRepository.deleteAll();
        clientRepository.deleteAll();
        bookRepository.deleteAll();
    }
}
