package com.example.library.book.model;

import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto mapToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setAvailable(book.isAvailable());
        return dto;
    }

}