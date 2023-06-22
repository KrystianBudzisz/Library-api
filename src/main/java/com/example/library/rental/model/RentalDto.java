package com.example.library.rental.model;

import com.example.library.book.model.Book;
import com.example.library.client.model.Client;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalDto {

    private Long id;
    private Long clientId;
    private Long bookId;
    private LocalDate start;
    private LocalDate end;
    private boolean returned;

//    public static RentalDto fromEntity(Rental rental) {
//        return RentalDto.builder()
//                .id(rental.getId())
//                .client(rental.getClient())
//                .book(rental.getBook())
//                .startDate(rental.getStartDate())
//                .endDate(rental.getEndDate())
//                .returned(rental.isReturned())
//                .build();
//    }
}
