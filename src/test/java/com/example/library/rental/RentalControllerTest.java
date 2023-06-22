package com.example.library.rental;

import com.example.library.rental.model.CreateRentalCommand;
import com.example.library.rental.model.RentalDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class RentalControllerTest {

    @Autowired
    private RentalController rentalController;

    @MockBean
    private RentalService rentalService;

    @Test
    void testCreateRental() {
        // Utwórz dane wejściowe żądania
        CreateRentalCommand createRentalCommand = new CreateRentalCommand();
        createRentalCommand.setClientId(1L);
        createRentalCommand.setBookId(1L);

        // Utwórz wynikowy RentalDto
        RentalDto expectedRentalDto = new RentalDto();
        expectedRentalDto.setId(1L);
        expectedRentalDto.setClientId(1L);
        expectedRentalDto.setBookId(1L);
        expectedRentalDto.setReturned(false);

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(rentalService.createRental(Mockito.any(CreateRentalCommand.class))).thenReturn(expectedRentalDto);

        // Wywołaj metodę createRental()
        ResponseEntity<RentalDto> responseEntity = rentalController.createRental(createRentalCommand);

        // Sprawdź, czy serwis został wywołany z poprawnym CreateRentalCommand
        Mockito.verify(rentalService, Mockito.times(1)).createRental(Mockito.any(CreateRentalCommand.class));

        // Sprawdź, czy zwrócone ResponseEntity ma poprawny status i DTO
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedRentalDto, responseEntity.getBody());
    }

    @Test
    void testReturnRental() {
        // Utwórz wynikowy RentalDto po zwrocie wypożyczenia
        RentalDto expectedReturnedRentalDto = new RentalDto();
        expectedReturnedRentalDto.setId(1L);
        expectedReturnedRentalDto.setClientId(1L);
        expectedReturnedRentalDto.setBookId(1L);
        expectedReturnedRentalDto.setReturned(true);

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(rentalService.returnRental(1L)).thenReturn(expectedReturnedRentalDto);

        // Wywołaj metodę returnRental()
        ResponseEntity<RentalDto> responseEntity = rentalController.returnRental(1L);

        // Sprawdź, czy serwis został wywołany z poprawnym ID
        Mockito.verify(rentalService, Mockito.times(1)).returnRental(1L);

        // Sprawdź, czy zwrócone ResponseEntity ma poprawny status i DTO
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedReturnedRentalDto, responseEntity.getBody());
    }

    @Test
    void testGetClientRentals() {
        // Utwórz dane testowe - wypożyczenia klienta
        List<RentalDto> rentals = new ArrayList<>();
        RentalDto rental1 = new RentalDto();
        rental1.setId(1L);
        rental1.setClientId(1L);
        rental1.setBookId(1L);
        rental1.setReturned(false);
        rentals.add(rental1);
        RentalDto rental2 = new RentalDto();
        rental2.setId(2L);
        rental2.setClientId(1L);
        rental2.setBookId(2L);
        rental2.setReturned(true);
        rentals.add(rental2);

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(rentalService.getClientRentals(1L)).thenReturn(rentals);

        // Wywołaj metodę getClientRentals()
        ResponseEntity<List<RentalDto>> responseEntity = rentalController.getClientRentals(1L);

        // Sprawdź, czy serwis został wywołany z poprawnym clientId
        Mockito.verify(rentalService, Mockito.times(1)).getClientRentals(1L);

        // Sprawdź, czy zwrócone ResponseEntity ma poprawny status i listę DTO
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(rentals, responseEntity.getBody());
    }
}
