package com.artwaha.springraphql.controller;

import com.artwaha.springraphql.model.Author;
import com.artwaha.springraphql.model.Book;
import com.artwaha.springraphql.model.dto.BookRequest;
import com.artwaha.springraphql.repository.AuthorRepository;
import com.artwaha.springraphql.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;


    @MutationMapping
    Book addBook(@Argument BookRequest book) {
        Author author = authorRepository.findById(book.getAuthorId()).orElseThrow();

        Book newBook = modelMapper.map(book, Book.class);
        newBook.setAuthor(author);

        return bookRepository.save(newBook);
    }
}
