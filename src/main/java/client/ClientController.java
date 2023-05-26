package client;

import book.model.BookDto;
import client.model.Client;
import client.model.ClientDto;
import client.model.CreateClientCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public Page<ClientDto> getClientsList(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        return clientService.findAll(PageRequest.of(page, size))
                .map(ClientDto::fromEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto createClient(@RequestBody CreateClientCommand command) {
        Client toSave = command.toEntity();
        return ClientDto.fromEntity(clientService.save(toSave));
    }

    @GetMapping("/{id}")
    public ClientDto findById(@PathVariable Long id) {
        return ClientDto.fromEntity(clientService.findById(id));
    }

    @GetMapping("/{id}/books")
    public List<BookDto> findBooksByClientId(@PathVariable Long id) {
        return clientService.findBooksByClientId(id)
                .stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }
}