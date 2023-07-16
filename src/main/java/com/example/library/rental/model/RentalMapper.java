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

}
