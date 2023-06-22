package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.BookMapper;
import com.example.library.book.model.CreateBookCommand;
import com.example.library.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    public BookDto createBook(CreateBookCommand createBookCommand) {
        Book book = new Book();
        book.setTitle(createBookCommand.getTitle());
        book.setAuthor(createBookCommand.getAuthor());
        book.setAvailable(true);  // książka jest dostępna do wypożyczenia po stworzeniu
        book = bookRepository.save(book);
        return bookMapper.mapToDto(book);
    }

    public BookDto blockBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setAvailable(false);  // książka nie jest dostępna do wypożyczenia po zablokowaniu
        book = bookRepository.save(book);
        return bookMapper.mapToDto(book);
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::mapToDto);
    }
}
