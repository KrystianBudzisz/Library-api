package com.example.library.rental;
import book.BookRepository;
import book.model.Book;
import customer.CustomerRepository;
import customer.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rental.RentalRepository;
import rental.model.Rental;
import rental.model.RentalService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void findAllByCustomer() {
        List<Rental> rentals = List.of(new Rental());
        when(rentalRepository.findAllByCustomer(1L)).thenReturn(rentals);

        List<Rental> result = rentalService.findAllByCustomer(1L);

        assertEquals(rentals, result);
    }

    @Test
    void rentBook() {
        Customer customer = new Customer(1L, "John", "Doe");
        Book book = new Book(1L, "Title", "Author", false);
        LocalDate rentalDate = LocalDate.now();
        LocalDate dueDate = LocalDate.now().plusDays(14);
        Rental rental = new Rental(null, customer, book, rentalDate, dueDate, null);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(rentalRepository.save(rental)).thenReturn(rental);

        Rental result = rentalService.rentBook(1L, 1L, rental);

        assertEquals(customer, result.getCustomer());
        assertEquals(book, result.getBook());
        assertEquals(rentalDate, result.getRentalDate());
        assertEquals(dueDate, result.getDueDate());
        assertNull(result.getReturnDate());
    }

    @Test
    void returnBook() {
        Rental rental = new Rental(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14), null);
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.returnBook(1L);

        assertNotNull(rental.getReturnDate());
        verify(rentalRepository, times(1)).save(rental);
    }
}
