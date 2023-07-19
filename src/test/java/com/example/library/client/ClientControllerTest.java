package com.example.library.client;

import com.example.library.client.model.Client;
import com.example.library.client.model.CreateClientCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;


    @BeforeEach
    void init() {

        Client clientToSave = Client.builder()
                .id(1L)
                .firstName("Krystiano")
                .lastName("Amigos")
                .build();
        clientRepository.saveAndFlush(clientToSave);
    }


    @Test
    public void shouldCreateClient() throws Exception {
        CreateClientCommand createClientCommand = new CreateClientCommand("TestFirstName", "TestLastName");
        String content = objectMapper.writeValueAsString(createClientCommand);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(createClientCommand.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(createClientCommand.getLastName()))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Long createdClientId = objectMapper.readValue(responseContent, Client.class).getId();

        Optional<Client> createdClientOptional = clientRepository.findById(createdClientId);
        assertTrue(createdClientOptional.isPresent());

        Client createdClient = createdClientOptional.get();
        assertEquals(createClientCommand.getFirstName(), createdClient.getFirstName());
        assertEquals(createClientCommand.getLastName(), createdClient.getLastName());
    }


    @Test
    public void shouldGetAllClients() throws Exception {
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1))) // Expecting only one client
                .andExpect(jsonPath("$.content[0].firstName").value("Krystiano"))
                .andExpect(jsonPath("$.content[0].lastName").value("Amigos"));

        List<Client> clientsInDb = clientRepository.findAll();
        assertEquals(1, clientsInDb.size());
        assertEquals("Krystiano", clientsInDb.get(0).getFirstName());
        assertEquals("Amigos", clientsInDb.get(0).getLastName());


    }


    @Test
    public void shouldGetClientById() throws Exception {
        Client client = new Client();
        client.setFirstName("TestFirstName");
        client.setLastName("TestLastName");
        client = clientRepository.save(client);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/" + client.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(client.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(client.getLastName()));

        Client retrievedClient = clientRepository.findById(client.getId()).orElse(null);
        assertNotNull(retrievedClient);
        assertEquals(client.getFirstName(), retrievedClient.getFirstName());
        assertEquals(client.getLastName(), retrievedClient.getLastName());
    }

    @AfterEach
    void teardown() {
        clientRepository.deleteAll();
    }
}
