package com.unir.ms_books_catalogue.data.seeder;

import com.unir.ms_books_catalogue.data.LibroJpaRepository;
import com.unir.ms_books_catalogue.data.model.Libro;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeederFake implements CommandLineRunner {

    private final LibroJpaRepository libroRepository;
    private void saveIfNotExist(Libro libro) {
        libroRepository.findByIsbn(libro.getIsbn()).ifPresentOrElse(
                existingLibro -> {
                    System.out.println("El libro con ISBN " + libro.getIsbn() + " ya existe.");
                },
                () -> {
                    libroRepository.save(libro);
                    System.out.println("Libro con ISBN " + libro.getIsbn() + " guardado.");
                }
        );
    }
    @Override
    public void run(String... args) throws Exception {
       //insertamos libros de pruebas
        saveIfNotExist(Libro.builder()
                .titulo("To Kill a Mockingbird")
                .autor("Harper Lee")
                .editorial("Planeta")
                .stock(10)
                .visible(true)
                .anio(1995)
                .isbn("978-0-06-112008-4")
                .resumen("A novel set in the Great Depression...")
                .genero("Fantasía")
                .precio(19.90f)
                .imagen("https://covers.openlibrary.org/b/id/8225261-L.jpg")
                .build());

        saveIfNotExist(Libro.builder()
                .titulo("1984")
                .autor("George Orwell")
                .editorial("Roca Editorial")
                .stock(0)
                .visible(true)
                .anio(1984)
                .isbn("978-0-452-28423-4")
                .resumen("A dystopian novel exploring themes...")
                .genero("Juvenil")
                .precio(16.50f)
                .imagen("https://covers.openlibrary.org/b/id/7222246-L.jpg")
                .build());

        saveIfNotExist(Libro.builder()
                .titulo("Pride and Prejudice")
                .autor("Jane Austen")
                .editorial("Siglo XXI Editores")
                .stock(20)
                .visible(false)
                .anio(2022)
                .isbn("978-0-19-953556-9")
                .resumen("A romantic novel that delves into issues of class...")
                .genero("Aventura")
                .precio(15.00f)
                .imagen("https://covers.openlibrary.org/b/id/8231856-L.jpg")
                .build());

        saveIfNotExist(Libro.builder()
                .titulo("The Great Gatsby")
                .autor("F. Scott Fitzgerald")
                .editorial("Santillana")
                .stock(7)
                .visible(true)
                .anio(1999)
                .isbn("978-0-7432-7356-5")
                .resumen("A story about the mysterious Jay Gatsby...")
                .genero("Ciencia")
                .precio(10.20f)
                .imagen("https://covers.openlibrary.org/b/id/7222161-L.jpg")
                .build());

        saveIfNotExist(Libro.builder()
                .titulo("Moby Dick")
                .autor("Herman Melville")
                .editorial("Anagrama")
                .stock(20)
                .visible(true)
                .anio(2004)
                .isbn("978-0-14-243724-7")
                .resumen("A thrilling adventure of Captain Ahab...")
                .genero("Ciencia Ficción")
                .precio(25.50f)
                .imagen("https://covers.openlibrary.org/b/id/7222247-L.jpg")
                .build());

        saveIfNotExist(Libro.builder()
                .titulo("The Catcher in the Rye")
                .autor("J.D. Salinger")
                .editorial("Alfaguara")
                .stock(6)
                .visible(true)
                .anio(2024)
                .isbn("978-0-316-76948-0")
                .resumen("The experiences of Holden Caulfield...")
                .genero("Biografías")
                .precio(16.50f)
                .imagen("https://covers.openlibrary.org/b/id/8226191-L.jpg")
                .build());

        saveIfNotExist(Libro.builder()
                .titulo("The Hobbit")
                .autor("J.R.R. Tolkien")
                .editorial("HarperCollins")
                .stock(7)
                .visible(false)
                .anio(2016)
                .isbn("978-0-618-26030-0")
                .resumen("The journey of Bilbo Baggins...")
                .genero("Terror")
                .precio(8.00f)
                .imagen("https://covers.openlibrary.org/b/id/6979861-L.jpg")
                .build());
    }
}