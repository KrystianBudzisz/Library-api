package com.example.library.rental;

import com.example.library.book.BookRepository;
import com.example.library.book.model.Book;
import com.example.library.client.ClientRepository;
import com.example.library.client.model.Client;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.rental.model.CreateRentalCommand;
import com.example.library.rental.model.Rental;
import com.example.library.rental.model.RentalDto;
import com.example.library.rental.model.RentalMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RentalService {

    private RentalRepository rentalRepository;

    private ClientRepository clientRepository;

    private BookRepository bookRepository;

    private RentalMapper rentalMapper;


    public RentalDto createRental(CreateRentalCommand createRentalCommand) {
        Client client = clientRepository.findById(createRentalCommand.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", createRentalCommand.getClientId()));
        Book book = bookRepository.findByIdForWrite(createRentalCommand.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", createRentalCommand.getBookId()));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book with id " + book.getId() + " is not available");
        }

        List<Rental> rentalsInGivenPeriod = rentalRepository
                .findByBookIdAndStartLessThanEqualAndEndGreaterThanEqual(
                        book.getId(), createRentalCommand.getEnd(), createRentalCommand.getStart());

        if (!rentalsInGivenPeriod.isEmpty()) {
            throw new IllegalStateException("Book with id " + book.getId() + " is not available in the given period");
        }

        Rental rental = new Rental();
        rental.setClient(client);
        rental.setBook(book);
        rental.setStart(createRentalCommand.getStart());
        rental.setEnd(createRentalCommand.getEnd());
        rental.setReturned(false);

        try {
            rental = rentalRepository.save(rental);
        } catch (OptimisticLockingFailureException ex) {
            throw new IllegalStateException("Cannot create rental due to concurrent modification.", ex);
        }

        return rentalMapper.mapToDto(rental);
    }


    public RentalDto returnRental(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rental", "id", id));
        rental.setReturned(true);
        rental = rentalRepository.save(rental);
        return rentalMapper.mapToDto(rental);
    }

    public List<RentalDto> getClientRentals(Long clientId) {
        List<Rental> rentals = rentalRepository.findByClientId(clientId);
        return rentals.stream().map(rentalMapper::mapToDto).collect(Collectors.toList());
    }
}
