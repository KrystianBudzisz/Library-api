package com.example.library.client;

import com.example.library.client.model.Client;
import com.example.library.client.model.ClientDto;
import com.example.library.client.model.ClientMapper;
import com.example.library.client.model.CreateClientCommand;
import com.example.library.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Test
    void testCreateClient() {
        // Utwórz dane wejściowe żądania
        CreateClientCommand createClientCommand = new CreateClientCommand("John", "Doe");

        // Utwórz klienta
        Client newClient = clientMapper.mapToEntity(createClientCommand);
        Client savedClient = new Client();
        savedClient.setId(1L);
        savedClient.setFirstName("John");
        savedClient.setLastName("Doe");

        // Zdefiniuj zachowanie mocka dla repository
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(savedClient);

        // Wywołaj metodę createClient() w serwisie
        ClientDto createdClient = clientService.createClient(createClientCommand);

        // Sprawdź, czy odpowiednie metody repository zostały wywołane
        Mockito.verify(clientRepository, Mockito.times(1)).save(Mockito.any(Client.class));

        // Sprawdź, czy zwrócony klient ma poprawne dane
        Assertions.assertEquals(savedClient.getId(), createdClient.getId());
        Assertions.assertEquals(savedClient.getFirstName(), createdClient.getFirstName());
        Assertions.assertEquals(savedClient.getLastName(), createdClient.getLastName());
    }

    @Test
    void testGetAllClients() {
        // Utwórz stronę z wynikami
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "lastName");

        // Utwórz listę klientów
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1L, "John", "Doe"));
        clients.add(new Client(2L, "Jane", "Smith"));

        // Zdefiniuj zachowanie mocka dla repository
        Mockito.when(clientRepository.findAll(pageable)).thenReturn(new PageImpl<>(clients));

        // Wywołaj metodę getAllClients() w serwisie
        Page<ClientDto> result = clientService.getAllClients(pageable);

        // Sprawdź, czy odpowiednia metoda repository została wywołana
        Mockito.verify(clientRepository, Mockito.times(1)).findAll(pageable);

        // Sprawdź, czy liczba klientów i ich dane są poprawne
        Assertions.assertEquals(clients.size(), result.getTotalElements());
        Assertions.assertEquals(clients.get(0).getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(clients.get(0).getFirstName(), result.getContent().get(0).getFirstName());
        Assertions.assertEquals(clients.get(0).getLastName(), result.getContent().get(0).getLastName());
        Assertions.assertEquals(clients.get(1).getId(), result.getContent().get(1).getId());
        Assertions.assertEquals(clients.get(1).getFirstName(), result.getContent().get(1).getFirstName());
        Assertions.assertEquals(clients.get(1).getLastName(), result.getContent().get(1).getLastName());
    }

    @Test
    void testGetClientById() {
        // Utwórz istniejącego klienta o znanym identyfikatorze
        Long clientId = 1L;
        Client client = new Client(clientId, "John", "Doe");

        // Zdefiniuj zachowanie mocka dla repository
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Wywołaj metodę getClientById() w serwisie
        ClientDto result = clientService.getClientById(clientId);

        // Sprawdź, czy odpowiednia metoda repository została wywołana
        Mockito.verify(clientRepository, Mockito.times(1)).findById(clientId);

        // Sprawdź, czy zwrócony klient ma poprawne dane
        Assertions.assertEquals(client.getId(), result.getId());
        Assertions.assertEquals(client.getFirstName(), result.getFirstName());
        Assertions.assertEquals(client.getLastName(), result.getLastName());
    }

    @Test
    void testGetClientById_InvalidId() {
        // Utwórz nieistniejącego klienta o nieznanym identyfikatorze
        Long clientId = 1L;

        // Zdefiniuj zachowanie mocka dla repository
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Wywołaj metodę getClientById() w serwisie i sprawdź, czy rzucony jest wyjątek ResourceNotFoundException
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(clientId);
        });

        // Sprawdź, czy odpowiednia metoda repository została wywołana
        Mockito.verify(clientRepository, Mockito.times(1)).findById(clientId);
    }
}
