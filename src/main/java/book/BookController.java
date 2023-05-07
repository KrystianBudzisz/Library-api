package book;

import book.model.Book;
import book.model.BookDto;
import book.model.CreateBookCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return bookService.findAll(PageRequest.of(page, size))
                .stream()
                .map(BookDto::fromEntity)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto addBook(@RequestBody @Valid CreateBookCommand createBookCommand) {
        Book book = createBookCommand.toEntity();
        return BookDto.fromEntity(bookService.save(book));
    }

    @PatchMapping("/{id}/block")
    public BookDto blockBook(@PathVariable Long id) {
        return BookDto.fromEntity(bookService.block(id));
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return BookDto.fromEntity(bookService.findById(id));
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody @Valid BookDto updatedBookDto) {
        return BookDto.fromEntity(bookService.update(id, updatedBookDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}

