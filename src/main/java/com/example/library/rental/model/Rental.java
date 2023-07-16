package com.example.library.rental.model;

import com.example.library.book.model.Book;
import com.example.library.client.model.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    private boolean returned;

    @Version
    private int version;

    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public Long getBookId() {
        return book != null ? book.getId() : null;
    }


}