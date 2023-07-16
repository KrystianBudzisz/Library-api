package com.example.library.rental;

import com.example.library.book.model.Book;
import com.example.library.client.model.Client;
import com.example.library.rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.client.id = :clientId")
    List<Rental> findByClientId(@Param("clientId") Long clientId);


    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId AND r.start <= :endDate AND r.end >= :startDate")
    List<Rental> findByBookIdAndStartLessThanEqualAndEndGreaterThanEqual(
            @Param("bookId") Long bookId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Optional<Rental> findByBookAndClient(Client client, Book book);
}
