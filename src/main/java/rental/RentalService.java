package rental;

import book.BookRepository;
import book.model.Book;
import client.ClientRepository;
import client.model.Client;
import exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rental.model.CreateRentalCommand;
import rental.model.Rental;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final ClientRepository clientRepository;
    private final BookRepository bookRepository;

    public Rental createRental(CreateRentalCommand command) {
        Client client = clientRepository.findById(command.getClientId())
                .orElseThrow(() -> new NotFoundException(Client.class.getSimpleName(), command.getClientId()));
        Book book = bookRepository.findById(command.getBookId())
                .orElseThrow(() -> new NotFoundException(Book.class.getSimpleName(), command.getBookId()));
        Rental rental = command.toEntity(client, book);
        return rentalRepository.save(rental);
    }

    public Rental returnBook(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Rental.class.getSimpleName(), id));
        rental.setReturned(true);
        return rentalRepository.save(rental);
    }

    public Page<Rental> findAllRentalsByClient(Long clientId, Pageable pageable) {
        return rentalRepository.findAllByClient_Id(clientId, pageable);
    }
}

