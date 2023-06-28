package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.BookMapper;
import com.example.library.book.model.CreateBookCommand;
import com.example.library.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookService {


    private BookRepository bookRepository;

    private BookMapper bookMapper;

    public BookDto createBook(CreateBookCommand createBookCommand) {
        Book book = new Book();
        book.setTitle(createBookCommand.getTitle());
        book.setAuthor(createBookCommand.getAuthor());
        book.setAvailable(true);
        book = bookRepository.save(book);
        return bookMapper.mapToDto(book);
    }

    public BookDto blockBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setAvailable(false);
        book = bookRepository.save(book);
        return bookMapper.mapToDto(book);
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::mapToDto);
    }
}
