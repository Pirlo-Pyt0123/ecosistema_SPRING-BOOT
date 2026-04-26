package com.unir.ms_books_catalogue.service;

import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.model.Libro;

import java.util.List;

public interface LibrosService {
    List<Libro> getLibros(String titulo, String autor, String editorial, Integer anio, String resumen, String genero, Boolean visible);

    Libro getLibro(String id);

    Boolean eliminarLibro(String id);

    Libro crearLibro(LibroDto libroCrear);

    Libro actualizarLibro(String id, String libroModificar);

    Libro actualizarLibro(String id, LibroDto libroModificar);


}