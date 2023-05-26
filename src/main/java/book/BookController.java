package book;

import book.model.Book;
import book.model.BookDto;
import book.model.CreateBookCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Page<BookDto> getBooksList(@RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return bookService.findAll(PageRequest.of(page, size))
                .map(BookDto::fromEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody CreateBookCommand command) {
        Book toSave = command.toEntity();
        return BookDto.fromEntity(bookService.save(toSave));
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        return BookDto.fromEntity(bookService.findById(id));
    }

    @PatchMapping("/{id}")
    public BookDto setBookAvailability(@PathVariable Long id, @RequestParam Boolean availability) {
        return BookDto.fromEntity(bookService.setBookAvailability(id, availability));
    }
}
