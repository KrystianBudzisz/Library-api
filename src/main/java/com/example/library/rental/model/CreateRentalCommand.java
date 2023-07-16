package com.example.library.rental.model;

import lombok.*;

import java.time.LocalDate;

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