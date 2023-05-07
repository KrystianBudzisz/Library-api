package book;

import book.model.Book;
import book.model.BookDto;
import exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public Book block(Long id) {
        Book book = findById(id);
        book.setBlocked(true);
        return bookRepository.save(book);
    }

    public Book update(Long id, BookDto updatedBookDto) {
        Book book = findById(id);
        book.setTitle(updatedBookDto.getTitle());
        book.setAuthor(updatedBookDto.getAuthor());
        book.setBlocked(updatedBookDto.isBlocked());
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }
}


