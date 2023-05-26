package client.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
public class CreateClientCommand {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    public Client toEntity() {
        return Client.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }
}

