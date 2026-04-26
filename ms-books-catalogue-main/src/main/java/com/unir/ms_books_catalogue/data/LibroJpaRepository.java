package com.unir.ms_books_catalogue.data;

import com.unir.ms_books_catalogue.data.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LibroJpaRepository extends JpaRepository<Libro, Long>, JpaSpecificationExecutor<Libro> {
    List<Libro> findByTitulo(String titulo);

    List<Libro> findByAutor(String autor);

    List<Libro> findByEditorial(String editorial);

    List<Libro> findByAnio(Integer anio);

    List<Libro> findByResumen(String resumen);

    List<Libro> findByGenero(String genero);

    List<Libro> findByTituloAndAutor(String titulo, String autor);

    Optional<Libro> findByIsbn(String isbn);

}
