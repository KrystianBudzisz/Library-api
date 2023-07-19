package com.example.library.rental;

import com.example.library.rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.client.id = :clientId")
    List<Rental> findByClientId(@Param("clientId") Long clientId);


    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rental r WHERE r.book.id = :bookId AND r.start <= :endDate AND r.end >= :startDate")
    boolean existsByBookIdAndStartLessThanEqualAndEndGreaterThanEqual(
            @Param("bookId") Long bookId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
