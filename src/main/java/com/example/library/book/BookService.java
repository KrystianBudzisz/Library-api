package com.example.library.book;


import com.example.library.book.model.Book;
import com.example.library.book.model.BookDto;
import com.example.library.book.model.BookMapper;
import com.example.library.book.model.CreateBookCommand;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookService {


    private BookRepository bookRepository;

    private BookMapper bookMapper;

    @Transactional
    public BookDto createBook(CreateBookCommand createBookCommand) {
        if (bookRepository.existsByTitle(createBookCommand.getTitle())) {
            throw new DuplicateResourceException("Book with title '" + createBookCommand.getTitle() + "' already exists");
        }

        Book book = new Book();
        book.setTitle(createBookCommand.getTitle());
        book.setAuthor(createBookCommand.getAuthor());
        book.setAvailable(true);

        try {
            book = bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("Book with title '" + createBookCommand.getTitle() + "' already exists");
        }

        return bookMapper.mapToDto(book);
    }


    @Transactional
    public void blockBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.blockBook(id);
    }


    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::mapToDto);
    }
}
