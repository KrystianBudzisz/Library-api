package com.example.library.client;

import com.example.library.client.model.ClientDto;
import com.example.library.client.model.CreateClientCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    void testCreateClient() throws Exception {
        // Utwórz dane wejściowe żądania
        CreateClientCommand createClientCommand = new CreateClientCommand("John", "Doe");


        // Utwórz klienta DTO do zwrócenia
        ClientDto createdClientDto = new ClientDto();
        createdClientDto.setId(1L);
        createdClientDto.setFirstName("John");
        createdClientDto.setLastName("Doe");

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(clientService.createClient(createClientCommand)).thenReturn(createdClientDto);

        // Wykonaj żądanie POST na /api/clients
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createClientCommand)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdClientDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createdClientDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createdClientDto.getLastName()));

        // Sprawdź, czy metoda createClient() w serwisie została wywołana raz
        Mockito.verify(clientService, Mockito.times(1)).createClient(createClientCommand);
    }

    @Test
    void testGetAllClients() throws Exception {
        // Utwórz listę klientów do zwrócenia
        List<ClientDto> clientDtoList = new ArrayList<>();
        ClientDto clientDto1 = new ClientDto();
        clientDto1.setId(1L);
        clientDto1.setFirstName("John");
        clientDto1.setLastName("Doe");
        clientDtoList.add(clientDto1);
        ClientDto clientDto2 = new ClientDto();
        clientDto2.setId(2L);
        clientDto2.setFirstName("Jane");
        clientDto2.setLastName("Smith");
        clientDtoList.add(clientDto2);

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(clientService.getAllClients(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(clientDtoList));

        // Wykonaj żądanie GET na /api/clients
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(clientDto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(clientDto1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(clientDto1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(clientDto2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value(clientDto2.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value(clientDto2.getLastName()));

        // Sprawdź, czy metoda getAllClients() w serwisie została wywołana raz
        Mockito.verify(clientService, Mockito.times(1)).getAllClients(Mockito.any(Pageable.class));
    }

    @Test
    void testGetClientById() throws Exception {
        // Utwórz klienta DTO do zwrócenia
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");

        // Zdefiniuj zachowanie mocka dla serwisu
        Mockito.when(clientService.getClientById(1L)).thenReturn(clientDto);

        // Wykonaj żądanie GET na /api/clients/{id}
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(clientDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(clientDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(clientDto.getLastName()));

        // Sprawdź, czy metoda getClientById() w serwisie została wywołana raz z odpowiednim parametrem
        Mockito.verify(clientService, Mockito.times(1)).getClientById(1L);
    }

    // Metoda pomocnicza do zamiany obiektu na JSON
    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
