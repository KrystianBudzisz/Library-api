package com.example.library.rental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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



}
