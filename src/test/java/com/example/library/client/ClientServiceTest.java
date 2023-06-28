package com.example.library.client;

import com.example.library.client.model.Client;
import com.example.library.client.model.ClientDto;
import com.example.library.client.model.ClientMapper;
import com.example.library.client.model.CreateClientCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void testCreateClient() {
        CreateClientCommand createClientCommand = new CreateClientCommand("John","Doe");


        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");

        when(clientMapper.mapToEntity(createClientCommand)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.mapToDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.createClient(createClientCommand);

        assertEquals(clientDto, result);
    }

    @Test
    public void testGetClientById() {
        Long id = 1L;

        Client client = new Client();
        client.setId(id);
        client.setFirstName("John");
        client.setLastName("Doe");

        ClientDto clientDto = new ClientDto();
        clientDto.setId(id);
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.mapToDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.getClientById(id);

        assertEquals(clientDto, result);
    }

    @Test
    public void testGetAllClients() {
        Pageable pageable = PageRequest.of(0, 20);

        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");

        List<Client> clients = Arrays.asList(client);

        Page<Client> clientPage = new PageImpl<>(clients, pageable, clients.size());

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
        when(clientMapper.mapToDto(client)).thenReturn(clientDto);

        Page<ClientDto> result = clientService.getAllClients(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(clientDto, result.getContent().get(0));
    }
}
