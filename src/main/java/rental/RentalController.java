package rental;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rental.model.CreateRentalCommand;
import rental.model.RentalDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalDto createRental(@RequestBody CreateRentalCommand command) {
        return RentalDto.fromEntity(rentalService.createRental(command));
    }

    @PutMapping("/{id}/return")
    public RentalDto returnBook(@PathVariable Long id) {
        return RentalDto.fromEntity(rentalService.returnBook(id));
    }

    @GetMapping("/client/{clientId}")
    public Page<RentalDto> findAllRentalsByClient(@PathVariable Long clientId,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        return rentalService.findAllRentalsByClient(clientId, PageRequest.of(page, size))
                .map(RentalDto::fromEntity);
    }

}
