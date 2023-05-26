package book.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class CreateBookCommand {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(this.title)
                .author(this.author)
                .isAvailable(true)
                .build();
    }
}