package client;

import book.model.Book;
import client.model.Client;

import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rental.model.Rental;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Client.class.getSimpleName(), id));
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public List<Book> findBooksByClientId(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Client.class.getSimpleName(), id))
                .getRentals()
                .stream()
                .map(Rental::getBook)
                .collect(Collectors.toList());
    }
}





