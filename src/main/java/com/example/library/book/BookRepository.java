package com.example.library.book;

import com.example.library.book.model.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdForWrite(@Param("id") Long id);

    Optional<Book> findByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.available = false WHERE b.id = :bookId")
    void blockBook(@Param("bookId") Long id);

    boolean existsByTitle(String title);
}
