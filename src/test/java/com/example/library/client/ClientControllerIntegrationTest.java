//package com.example.library.client;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import customer.ClientRepository;
//import customer.model.Client;
//import customer.model.ClientDto;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//class ClientControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void init() {
//        Client clientToSave = new Client("John", "Doe");
//        clientRepository.saveAndFlush(clientToSave);
//    }
//
//    @Test
//    void shouldGetAllClients() throws Exception {
//        mockMvc.perform(get("/api/clients"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$.[0].firstName").value("John"))
//                .andExpect(jsonPath("$.[0].lastName").value("Doe"));
//    }
//
//    @Test
//    void shouldFindById() throws Exception {
//        mockMvc.perform(get("/api/clients/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.lastName").value("Doe"));
//    }
//
//    @Test
//    void shouldCreateClient() throws Exception {
//        ClientDto clientDto = new ClientDto();
//        clientDto.setFirstName("Alice");
//        clientDto.setLastName("Smith");
//
//        mockMvc.perform(post("/api/clients")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(clientDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.firstName").value("Alice"))
//                .andExpect(jsonPath("$.lastName").value("Smith"));
//    }
//
//    @Test
//    void shouldDeleteById() throws Exception {
//        mockMvc.perform(delete("/api/clients/1"))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//
//    @AfterEach
//    void teardown() {
//        clientRepository.deleteAll();
//    }
//}
