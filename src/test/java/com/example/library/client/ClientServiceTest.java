//package com.example.library.client;
//
//import client.ClientRepository;
//import client.ClientService;
//import client.model.Client;
//import exception.NotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ClientServiceTest {
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @InjectMocks
//    private ClientService clientService;
//
//    private Client client;
//
//    @BeforeEach
//    public void setUp() {
//        client = Client.builder()
//                .id(1L)
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//    }
//
//    @Test
//    public void testFindById() {
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
//
//        Client foundClient = clientService.findById(1L);
//
//        assertEquals(client, foundClient);
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testFindById_NotFound() {
//        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> clientService.findById(1L));
//        verify(clientRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testSave() {
//        when(clientRepository.save(any(Client.class))).thenReturn(client);
//
//        Client createdClient = clientService.save(client);
//
//        assertEquals(client, createdClient);
//        verify(clientRepository, times(1)).save(any(Client.class));
//    }
//
//    @Test
//    public void testFindAll() {
//        Pageable pageable = PageRequest.of(0, 5);
//        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client), pageable, 1);
//        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
//
//        Page<Client> foundClients = clientService.findAll(pageable);
//
//        assertEquals(clientPage, foundClients);
//        verify(clientRepository, times(1)).findAll(pageable);
//    }
//}
//
