package com.example.library.rental.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateRentalCommand {

    private Long clientId;
    private Long bookId;
    private LocalDate start;
    private LocalDate end;
}