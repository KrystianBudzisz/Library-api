package com.example.library.client;

import com.example.library.client.model.ClientDto;
import com.example.library.client.model.CreateClientCommand;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody CreateClientCommand createClientCommand) {
        ClientDto createdClientDto = clientService.createClient(createClientCommand);
        return ResponseEntity.ok(createdClientDto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> getAllClients(Pageable pageable) {
        Page<ClientDto> clientDtoPage = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clientDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto clientDto = clientService.getClientById(id);
        return ResponseEntity.ok(clientDto);
    }
}
