package com.artwaha.springraphql.repository;

import com.artwaha.springraphql.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleIgnoreCase(String title);
}