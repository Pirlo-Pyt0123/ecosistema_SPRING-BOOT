package com.unir.ms_books_catalogue.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LibroDto {
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anio;
    private String isbn;
    private String resumen;
    private String genero;
    private Float precio;
    private String imagen;
    private Integer stock;
    private Boolean visible;
    private Integer valoracion;
}
