package com.example.library.client;

import com.example.library.client.model.Client;
import com.example.library.client.model.ClientDto;
import com.example.library.client.model.ClientMapper;
import com.example.library.client.model.CreateClientCommand;
import com.example.library.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientDto createClient(CreateClientCommand createClientCommand) {
        Client newClient = clientMapper.mapToEntity(createClientCommand);
        Client savedClient = clientRepository.save(newClient);
        return clientMapper.mapToDto(savedClient);
    }

    public Page<ClientDto> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::mapToDto);
    }

    public ClientDto getClientById(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        Client client = clientOptional.orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        return clientMapper.mapToDto(client);
    }
}
