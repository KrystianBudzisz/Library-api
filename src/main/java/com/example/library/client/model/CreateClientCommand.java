package com.example.library.client.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CreateClientCommand {

        @NotBlank(message = "firstName cannot be blank")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank")
    private String lastName;

    public Client toEntity() {
        return Client.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .build();
    }
}

