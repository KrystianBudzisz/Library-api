package com.example.library.rental;

import com.example.library.book.BookRepository;
import com.example.library.book.model.Book;
import com.example.library.client.ClientRepository;
import com.example.library.client.model.Client;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.rental.model.CreateRentalCommand;
import com.example.library.rental.model.Rental;
import com.example.library.rental.model.RentalDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RentalServiceTest {

    @Autowired
    private RentalService rentalService;

    @MockBean
    private RentalRepository rentalRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void testCreateRental() {
        CreateRentalCommand createRentalCommand = new CreateRentalCommand();
        createRentalCommand.setClientId(1L);
        createRentalCommand.setBookId(1L);
        createRentalCommand.setStart(LocalDate.now());
        createRentalCommand.setEnd(LocalDate.now().plusDays(7));

        Client client = new Client();
        client.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        Rental expectedRental = new Rental();
        expectedRental.setId(1L);
        expectedRental.setClient(client);
        expectedRental.setBook(book);
        expectedRental.setStart(createRentalCommand.getStart());
        expectedRental.setEnd(createRentalCommand.getEnd());
        expectedRental.setReturned(false);

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(bookRepository.findByIdForWrite(1L)).thenReturn(Optional.of(book));

        ArgumentCaptor<Rental> rentalCaptor = ArgumentCaptor.forClass(Rental.class);
        Mockito.when(rentalRepository.save(rentalCaptor.capture())).thenReturn(expectedRental);

        RentalDto rentalDto = rentalService.createRental(createRentalCommand);

        Mockito.verify(clientRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).findByIdForWrite(1L);
        Mockito.verify(rentalRepository, Mockito.times(1)).save(Mockito.any(Rental.class));

        Rental capturedRental = rentalCaptor.getValue();
        Assertions.assertEquals(expectedRental.getClient(), capturedRental.getClient());
        Assertions.assertEquals(expectedRental.getBook(), capturedRental.getBook());
        Assertions.assertEquals(expectedRental.getStart(), capturedRental.getStart());
        Assertions.assertEquals(expectedRental.getEnd(), capturedRental.getEnd());
        Assertions.assertEquals(expectedRental.isReturned(), capturedRental.isReturned());

        Assertions.assertEquals(expectedRental.getId(), rentalDto.getId());
        Assertions.assertEquals(expectedRental.getClient().getId(), rentalDto.getClientId());
        Assertions.assertEquals(expectedRental.getBook().getId(), rentalDto.getBookId());
        Assertions.assertEquals(expectedRental.isReturned(), rentalDto.isReturned());
    }


    @Test
    void testCreateRental_whenTwoClientsRentSameBook_thenThrowsException() {
        CreateRentalCommand createRentalCommand1 = new CreateRentalCommand();
        createRentalCommand1.setClientId(1L);
        createRentalCommand1.setBookId(1L);
        createRentalCommand1.setStart(LocalDate.now());
        createRentalCommand1.setEnd(LocalDate.now().plusDays(7));

        CreateRentalCommand createRentalCommand2 = new CreateRentalCommand();
        createRentalCommand2.setClientId(2L);
        createRentalCommand2.setBookId(1L);
        createRentalCommand2.setStart(LocalDate.now());
        createRentalCommand2.setEnd(LocalDate.now().plusDays(7));

        Client client1 = new Client();
        client1.setId(1L);

        Client client2 = new Client();
        client2.setId(2L);

        Book book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        Rental expectedRental = new Rental();
        expectedRental.setId(1L);
        expectedRental.setClient(client1);
        expectedRental.setBook(book);
        expectedRental.setStart(createRentalCommand1.getStart());
        expectedRental.setEnd(createRentalCommand1.getEnd());
        expectedRental.setReturned(false);

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        Mockito.when(clientRepository.findById(2L)).thenReturn(Optional.of(client2));
        Mockito.when(bookRepository.findByIdForWrite(1L)).thenReturn(Optional.of(book));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(expectedRental);

        RentalDto rentalDto = rentalService.createRental(createRentalCommand1);

        Assertions.assertEquals(expectedRental.getId(), rentalDto.getId());
        Assertions.assertEquals(expectedRental.getClient().getId(), rentalDto.getClientId());
        Assertions.assertEquals(expectedRental.getBook().getId(), rentalDto.getBookId());
        Assertions.assertEquals(expectedRental.isReturned(), rentalDto.isReturned());

        Mockito.when(rentalRepository.findByBookIdAndStartLessThanEqualAndEndGreaterThanEqual(
                        1L, createRentalCommand2.getEnd(), createRentalCommand2.getStart()))
                .thenReturn(Collections.singletonList(expectedRental));

        Assertions.assertThrows(IllegalStateException.class, () -> rentalService.createRental(createRentalCommand2));
    }

    @Test
    void testCreateRental_whenClientNotFound_thenThrowsException() {
        CreateRentalCommand createRentalCommand = new CreateRentalCommand();
        createRentalCommand.setClientId(1L);
        createRentalCommand.setBookId(1L);
        createRentalCommand.setStart(LocalDate.now());
        createRentalCommand.setEnd(LocalDate.now().plusDays(7));

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> rentalService.createRental(createRentalCommand));
    }

    @Test
    void testCreateRental_whenBookNotFound_thenThrowsException() {
        CreateRentalCommand createRentalCommand = new CreateRentalCommand();
        createRentalCommand.setClientId(1L);
        createRentalCommand.setBookId(1L);
        createRentalCommand.setStart(LocalDate.now());
        createRentalCommand.setEnd(LocalDate.now().plusDays(7));

        Client client = new Client();
        client.setId(1L);

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(bookRepository.findByIdForWrite(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> rentalService.createRental(createRentalCommand));
    }


    @Test
    void testReturnRental() {
        Client client = new Client();
        client.setId(1L);

        Book book = new Book();
        book.setId(1L);

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturned(false);
        rental.setClient(client);
        rental.setBook(book);

        Rental returnedRental = new Rental();
        returnedRental.setId(1L);
        returnedRental.setReturned(true);
        returnedRental.setClient(client);
        returnedRental.setBook(book);

        Mockito.when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        ArgumentCaptor<Rental> rentalCaptor = ArgumentCaptor.forClass(Rental.class);
        Mockito.when(rentalRepository.save(rentalCaptor.capture())).thenReturn(returnedRental);

        RentalDto rentalDto = rentalService.returnRental(1L);

        Mockito.verify(rentalRepository, Mockito.times(1)).findById(1L);

        Mockito.verify(rentalRepository, Mockito.times(1)).save(Mockito.any(Rental.class));

        Rental capturedRental = rentalCaptor.getValue();
        Assertions.assertTrue(capturedRental.isReturned());

        Assertions.assertEquals(returnedRental.getId(), rentalDto.getId());
        Assertions.assertEquals(returnedRental.isReturned(), rentalDto.isReturned());
    }


    @Test
    void testGetClientRentals() {
        Client client = new Client();
        client.setId(1L);

        Book book = new Book();
        book.setId(1L);

        List<Rental> rentals = new ArrayList<>();
        Rental rental1 = new Rental();
        rental1.setId(1L);
        rental1.setClient(client);
        rental1.setBook(book);
        rental1.setReturned(false);
        rentals.add(rental1);
        Rental rental2 = new Rental();
        rental2.setId(2L);
        rental2.setClient(client);
        rental2.setBook(book);
        rental2.setReturned(true);
        rentals.add(rental2);

        Mockito.when(rentalRepository.findByClientId(1L)).thenReturn(rentals);

        List<RentalDto> rentalDtos = rentalService.getClientRentals(1L);

        Mockito.verify(rentalRepository, Mockito.times(1)).findByClientId(1L);

        Assertions.assertEquals(rentals.size(), rentalDtos.size());
        Assertions.assertEquals(rentals.get(0).getId(), rentalDtos.get(0).getId());
        Assertions.assertEquals(rentals.get(0).isReturned(), rentalDtos.get(0).isReturned());
        Assertions.assertEquals(rentals.get(1).getId(), rentalDtos.get(1).getId());
        Assertions.assertEquals(rentals.get(1).isReturned(), rentalDtos.get(1).isReturned());
    }


}
