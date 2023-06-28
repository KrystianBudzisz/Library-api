package com.example.library.client.model;

import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDto mapToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        return dto;
    }

    public Client mapToEntity(CreateClientCommand createClientCommand) {
        Client client = new Client();
        client.setFirstName(createClientCommand.getFirstName());
        client.setLastName(createClientCommand.getLastName());
        return client;
    }
}
