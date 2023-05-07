package rental.model;

import book.BookRepository;
import book.model.Book;
import customer.CustomerRepository;
import customer.model.Customer;
import exception.BookNotFoundException;
import exception.CustomerNotFoundException;
import exception.RentalNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rental.RentalRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    public List<Rental> findAllByCustomer(Long customerId) {
        return rentalRepository.findAllByCustomer(customerId);
    }

    public Rental rentBook(Long customerId, Long bookId, Rental rental) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.isBlocked()) {
            throw new IllegalStateException("The book is currently blocked and cannot be rented");
        }

        rental.setCustomer(customer);
        rental.setBook(book);
        return rentalRepository.save(rental);
    }

    public void returnBook(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("The book has already been returned");
        }

        rental.setReturnDate(LocalDate.now());
        rentalRepository.save(rental);
    }
}

