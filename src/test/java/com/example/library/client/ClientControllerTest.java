package com.example.library.client;

import com.example.library.client.model.Client;
import com.example.library.client.model.CreateClientCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClientControllerTest {
    private static Client client;

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
        client = clientToSave;
        clientRepository.saveAndFlush(clientToSave);
    }


    @Test
    public void shouldCreateClient() throws Exception {
        CreateClientCommand createClientCommand = new CreateClientCommand("TestFirstName", "TestLastName");
        String content = objectMapper.writeValueAsString(createClientCommand);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
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
        MvcResult result = mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].firstName").value("Krystiano"))
                .andExpect(jsonPath("$.content[0].lastName").value("Amigos"))
                .andReturn();

        List<Client> clients = clientRepository.findAll();
        assertEquals(2, clients.size());
        assertEquals("Krystiano", clients.get(0).getFirstName());
        assertEquals("Amigos", clients.get(0).getLastName());
        assertEquals("Jane", clients.get(1).getFirstName());
        assertEquals("Doe", clients.get(1).getLastName());
    }


    @Test
    public void shouldGetClientById() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Krystiano"))
                .andExpect(jsonPath("$.lastName").value("Amigos"))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Long fetchedClientId = objectMapper.readValue(responseContent, Client.class).getId();

        Optional<Client> fetchedClientOptional = clientRepository.findById(fetchedClientId);
        assertTrue(fetchedClientOptional.isPresent());

        Client fetchedClient = fetchedClientOptional.get();
        assertEquals("Krystiano", fetchedClient.getFirstName());
        assertEquals("Amigos", fetchedClient.getLastName());

    }


    @AfterEach
    void teardown() {
        clientRepository.deleteAll();
    }
}
