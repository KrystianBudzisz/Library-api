package book;

import book.model.Book;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Book.class.getSimpleName(), id));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book setBookAvailability(Long id, Boolean availability) {
        Book book = findById(id);
        book.setAvailable(availability);
        return bookRepository.save(book);
    }
}



