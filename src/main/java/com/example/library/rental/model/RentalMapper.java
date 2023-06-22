package com.example.library.rental.model;

import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public RentalDto mapToDto(Rental rental) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        dto.setClientId(rental.getClient().getId());
        dto.setBookId(rental.getBook().getId());
        dto.setStart(rental.getStart());
        dto.setEnd(rental.getEnd());
        dto.setReturned(rental.isReturned());
        return dto;
    }

    public Rental mapToEntity(RentalDto rentalDto) {
        Rental rental = new Rental();
        // Pamiętaj, że musisz ustawić Klienta i Książkę, nie tylko ich identyfikatory.
        // Prawdopodobnie będziesz musiał użyć repozytoriów lub serwisów do pobrania tych encji.
        // W tym przypadku pominąłem te kroki, ponieważ są one specyficzne dla twojego konkretnego projektu.
        rental.setStart(rentalDto.getStart());
        rental.setEnd(rentalDto.getEnd());
        rental.setReturned(rentalDto.isReturned());
        return rental;
    }
}
