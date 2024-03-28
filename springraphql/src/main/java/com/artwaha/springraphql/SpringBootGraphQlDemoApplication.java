package com.artwaha.springraphql;

import com.artwaha.springraphql.model.Author;
import com.artwaha.springraphql.model.Book;
import com.artwaha.springraphql.repository.AuthorRepository;
import com.artwaha.springraphql.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringBootGraphQlDemoApplication implements ApplicationRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGraphQlDemoApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /* Authors */
        Author sherlockHolmes = Author.builder().name("Sherlock Holmes").build();
        Author jamesBond = Author.builder().name("James Bond").build();

        if (!authorRepository.existsByNameIgnoreCase(sherlockHolmes.getName()) && !authorRepository.existsByNameIgnoreCase(sherlockHolmes.getName())) {
            authorRepository.saveAll(List.of(sherlockHolmes, jamesBond));
        }

        /* Books */
        Book abominableBride = Book.builder().title("The Abominable Bride").publisher("John Watson").author(sherlockHolmes).build();
        Book scandalInBelgravia = Book.builder().title("Scandal in Belgravia").publisher("John Watson").author(sherlockHolmes).build();
        Book houndsOfBaskerville = Book.builder().title("The Hounds of Baskerville").publisher("John Watson").author(sherlockHolmes).build();

        Book casinoRoyale = Book.builder().title("Casino Royale").publisher("Daniel Craig").author(jamesBond).build();
        Book skyfall = Book.builder().title("Skyfall").publisher("Daniel Craig").author(jamesBond).build();
        Book spectre = Book.builder().title("Spectre").publisher("Daniel Craig").author(jamesBond).build();

        if (!bookRepository.existsByTitleIgnoreCase(abominableBride.getTitle()) && !bookRepository.existsByTitleIgnoreCase(scandalInBelgravia.getTitle()) && !bookRepository.existsByTitleIgnoreCase(houndsOfBaskerville.getTitle()) && !bookRepository.existsByTitleIgnoreCase(casinoRoyale.getTitle()) && !bookRepository.existsByTitleIgnoreCase(skyfall.getTitle()) && !bookRepository.existsByTitleIgnoreCase(spectre.getTitle())) {
            bookRepository.saveAll(List.of(abominableBride, scandalInBelgravia, houndsOfBaskerville, casinoRoyale, skyfall, spectre));
        }
    }
}
