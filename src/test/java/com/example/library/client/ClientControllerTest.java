package com.example.library.client;

import com.example.library.client.model.ClientDto;
import com.example.library.client.model.CreateClientCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Test
    void testCreateClient() throws Exception {
        CreateClientCommand createClientCommand = new CreateClientCommand("John", "Doe");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createClientCommand)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetAllClients() throws Exception {
        CreateClientCommand createClientCommand1 = new CreateClientCommand("John", "Doe");

        CreateClientCommand createClientCommand2 = new CreateClientCommand("John", "Doe");


        clientService.createClient(createClientCommand1);
        clientService.createClient(createClientCommand2);

        Page<ClientDto> clients = clientService.getAllClients(PageRequest.of(0, 5));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        Matchers.hasSize(Math.toIntExact(clients.getTotalElements()))));
    }

    @Test
    void testGetClientById() throws Exception {
        CreateClientCommand createClientCommand = new CreateClientCommand("John", "Doe");

        ClientDto createdClientDto = clientService.createClient(createClientCommand);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/{id}", createdClientDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdClientDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(createdClientDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(createdClientDto.getLastName()));
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
