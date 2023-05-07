package book.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateBookCommand {


    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private boolean blocked;

    public Book toEntity() {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setBlocked(blocked);
        return book;
    }

}

