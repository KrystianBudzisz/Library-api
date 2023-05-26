package rental.model;

import book.model.Book;
import book.model.BookDto;
import client.model.Client;
import client.model.ClientDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RentalDto {

    private Long id;
    private Client client;
    private Book book;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean returned;

    public static RentalDto fromEntity(Rental rental) {
        return RentalDto.builder()
                .id(rental.getId())
                .client(rental.getClient())
                .book(rental.getBook())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .returned(rental.isReturned())
                .build();
    }
}
