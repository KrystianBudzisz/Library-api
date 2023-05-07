package customer;

import customer.model.CreateCustomerCommand;
import customer.model.Customer;
import customer.model.CustomerDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDto> getAllCustomers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return customerService.findAll(PageRequest.of(page, size))
                .stream()
                .map(CustomerDto::fromEntity)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto addCustomer(@RequestBody @Valid CreateCustomerCommand createCustomerCommand) {
        Customer customer = createCustomerCommand.toEntity();
        return CustomerDto.fromEntity(customerService.save(customer));
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id) {
        return CustomerDto.fromEntity(customerService.findById(id));
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerDto updatedCustomerDto) {
        return CustomerDto.fromEntity(customerService.update(id, updatedCustomerDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
    }
}



