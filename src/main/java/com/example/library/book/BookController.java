package com.example.library.book;


import com.example.library.book.model.BookDto;
import com.example.library.book.model.CreateBookCommand;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody CreateBookCommand createBookCommand) {
        BookDto createdBook = bookService.createBook(createBookCommand);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockBook(@PathVariable Long id) {
        bookService.blockBook(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {
        Page<BookDto> books = bookService.getAllBooks(pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
