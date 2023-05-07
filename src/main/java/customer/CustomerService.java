package customer;

import customer.model.Customer;
import customer.model.CustomerDto;
import exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }


    public Customer update(Long id, CustomerDto updatedCustomerDto) {
        Customer customer = findById(id);
        customer.setFirstName(updatedCustomerDto.getFirstName());
        customer.setLastName(updatedCustomerDto.getLastName());
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        Customer customer = findById(id);
        customerRepository.delete(customer);
    }
}



