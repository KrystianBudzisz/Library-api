package com.example.library.book.model;

import com.example.library.rental.model.Rental;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private boolean available;

    @OneToMany(mappedBy = "book")
    private Set<Rental> rentals;

    @Version
    @Column(nullable = true, columnDefinition = "integer default 0")
    private Integer version;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;



}
