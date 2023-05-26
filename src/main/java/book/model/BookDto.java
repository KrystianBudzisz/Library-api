package book.model;

import lombok.*;

@Getter
@Builder
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private boolean isAvailable;

    public static BookDto fromEntity(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isAvailable(book.isAvailable())
                .build();
    }
}


