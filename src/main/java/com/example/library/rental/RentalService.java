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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentalMapper rentalMapper;

    public RentalDto createRental(CreateRentalCommand createRentalCommand) {
        Client client = clientRepository.findById(createRentalCommand.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", createRentalCommand.getClientId()));
        Book book = bookRepository.findById(createRentalCommand.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", createRentalCommand.getBookId()));
        Rental rental = new Rental();
        rental.setClient(client);
        rental.setBook(book);
        rental.setStart(createRentalCommand.getStart());
        rental.setEnd(createRentalCommand.getEnd());
        rental.setReturned(false);
        rental = rentalRepository.save(rental);
        return rentalMapper.mapToDto(rental);
    }

    public RentalDto returnRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "id", id));
        rental.setReturned(true);
        rental = rentalRepository.save(rental);
        return rentalMapper.mapToDto(rental);
    }

    public List<RentalDto> getClientRentals(Long clientId) {
        List<Rental> rentals = rentalRepository.findByClientId(clientId);
        return rentals.stream()
                .map(rentalMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
