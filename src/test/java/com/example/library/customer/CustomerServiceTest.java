package com.example.library.customer;

import customer.CustomerRepository;
import customer.CustomerService;
import customer.model.Customer;
import customer.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Customer> customers = List.of(
                new Customer(1L, "John", "Doe"),
                new Customer(2L, "Jane", "Doe")
        );
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> result = customerService.findAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("Doe", result.getContent().get(0).getLastName());
        assertEquals("Jane", result.getContent().get(1).getFirstName());
        assertEquals("Doe", result.getContent().get(1).getLastName());
    }

    @Test
    void save() {
        Customer customerToSave = new Customer(null, "John", "Doe");
        Customer savedCustomer = new Customer(1L, "John", "Doe");
        when(customerRepository.save(customerToSave)).thenReturn(savedCustomer);

        Customer result = customerService.save(customerToSave);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void findById() {
        Customer customer = new Customer(1L, "John", "Doe");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void update() {
        Customer originalCustomer = new Customer(1L, "John", "Doe");
        CustomerDto updatedCustomerDto = new CustomerDto(1L, "Jane", "Doe");
        Customer updatedCustomer = new Customer(1L, "Jane", "Doe");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(originalCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.update(1L, updatedCustomerDto);

        assertEquals(1L, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void delete() {
        Customer customerToDelete = new Customer(1L, "John", "Doe");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customerToDelete));
        doNothing().when(customerRepository).delete(customerToDelete);

        customerService.delete(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(customerToDelete);
    }
}

