//package com.example.library.rental;
//
//import book.BookRepository;
//import book.model.Book;
//import client.ClientRepository;
//import client.model.Client;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import rental.RentalRepository;
//import rental.RentalService;
//import rental.model.Rental;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class RentalServiceTest {
//
//    @Mock
//    private RentalRepository rentalRepository;
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @InjectMocks
//    private RentalService rentalService;
//
//    private Rental rental;
//    private Book book;
//    private Client client;
//
//    @BeforeEach
//    public void setUp() {
//        book = Book.builder()
//                .id(1L)
//                .title("Example Book")
//                .author("John Doe")
//                .available(true)
//                .build();
//
//        client = Client.builder()
//                .id(1L)
//                .firstName("Jane")
//                .lastName("Doe")
//                .build();
//
//        rental = Rental.builder()
//                .id(1L)
//                .client(client)
//                .book(book)
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.now().plusDays(14))
//                .build();
//    }
//
//    @Test
//    public void testSave() {
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//        when(bookRepository.save(book)).thenReturn(book);
//        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);
//
//        Rental savedRental = rentalService.save(rental);
//
//        assertEquals(rental, savedRental);
//        verify(clientRepository, times(1)).findById(1L);
//        verify(bookRepository, times(1)).findById(1L);
//        verify(bookRepository, times(1)).save(book);
//        verify(rentalRepository, times(1)).save(any(Rental.class));
//    }
//
//    @Test
//    public void testReturnBook() {
//        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
//        when(bookRepository.save(book)).thenReturn(book);
//        when(rentalRepository.save(rental)).thenReturn(rental);
//
//        Rental returnedRental = rentalService.returnBook(1L);
//
//        assertTrue(returnedRental.isReturned());
//        assertTrue(book.isAvailable());
//        verify(rentalRepository, times(1)).findById(1L);
//        verify(bookRepository, times(1)).save(book);
//        verify(rentalRepository, times(1)).save(rental);
//    }
//}
