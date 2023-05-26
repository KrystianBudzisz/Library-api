package rental.model;

import book.model.Book;
import client.model.Client;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateRentalCommand {

    private Long clientId;

    private Long bookId;

    private LocalDate startDate;

    private LocalDate endDate;

    public Rental toEntity(Client client, Book book) {
        return Rental.builder()
                .client(client)
                .book(book)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .returned(false)
                .build();
    }
}

