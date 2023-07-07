package com.example.library.rental;

import com.example.library.book.BookRepository;
import com.example.library.book.model.Book;
import com.example.library.client.ClientRepository;
import com.example.library.client.model.Client;
import com.example.library.rental.model.CreateRentalCommand;
import com.example.library.rental.model.Rental;
import com.example.library.rental.model.RentalDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
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
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(expectedRental);

        RentalDto rentalDto = rentalService.createRental(createRentalCommand);

        Mockito.verify(clientRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(rentalRepository, Mockito.times(1)).save(Mockito.any(Rental.class));

        Assertions.assertEquals(expectedRental.getId(), rentalDto.getId());
        Assertions.assertEquals(expectedRental.getClient().getId(), rentalDto.getClientId());
        Assertions.assertEquals(expectedRental.getBook().getId(), rentalDto.getBookId());
        Assertions.assertEquals(expectedRental.isReturned(), rentalDto.isReturned());
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
        Mockito.when(rentalRepository.save(Mockito.any(Rental.class))).thenReturn(returnedRental);

        RentalDto rentalDto = rentalService.returnRental(1L);

        Mockito.verify(rentalRepository, Mockito.times(1)).findById(1L);

        Mockito.verify(rentalRepository, Mockito.times(1)).save(Mockito.any(Rental.class));

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
