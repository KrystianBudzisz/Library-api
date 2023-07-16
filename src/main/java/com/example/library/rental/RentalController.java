package com.example.library.rental;

import com.example.library.rental.model.CreateRentalCommand;
import com.example.library.rental.model.RentalDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestBody CreateRentalCommand createRentalCommand) {
        RentalDto createdRental = rentalService.createRental(createRentalCommand);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<RentalDto> returnRental(@PathVariable Long id) {
        RentalDto returnedRental = rentalService.returnRental(id);
        return new ResponseEntity<>(returnedRental, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RentalDto>> getClientRentals(@PathVariable Long clientId) {
        List<RentalDto> rentals = rentalService.getClientRentals(clientId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }
}
