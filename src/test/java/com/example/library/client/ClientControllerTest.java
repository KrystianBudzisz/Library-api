package com.example.library.client;

import com.example.library.client.model.Client;
import com.example.library.client.model.CreateClientCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void createClient() throws Exception {
        CreateClientCommand createClientCommand = new CreateClientCommand("TestFirstName", "TestLastName");
        String content = objectMapper.writeValueAsString(createClientCommand);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void getAllClients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(greaterThanOrEqualTo(0))))
                .andDo(print());
    }

    @Test
    public void getClientById() throws Exception {
        Client newClient = new Client();
        newClient.setFirstName("TestFirstName");
        newClient.setLastName("TestLastName");
        Client savedClient = clientRepository.save(newClient);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/" + savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(savedClient.getId().intValue())))
                .andDo(print());
    }
    @AfterEach
    void teardown() {
        clientRepository.deleteAll();
    }
}
