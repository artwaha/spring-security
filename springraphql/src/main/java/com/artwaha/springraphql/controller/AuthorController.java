package com.artwaha.springraphql.controller;

import com.artwaha.springraphql.model.Author;
import com.artwaha.springraphql.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorRepository authorRepository;

    @QueryMapping
    List<Author> authors() {
        return authorRepository.findAll();
    }

    @QueryMapping
    Author authorById(@Argument Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid Author Id"));
    }
}
