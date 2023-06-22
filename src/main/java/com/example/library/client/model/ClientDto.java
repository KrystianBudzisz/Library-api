package com.example.library.client.model;

import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ClientDto {

    private Long id;
    private String firstName;
    private String lastName;


}