package com.unir.ms_books_catalogue.data.model;

import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.utils.Constantes;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Table(name = "libros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = Constantes.TITULO, nullable = false)
    private String titulo;

    @Column(name = Constantes.AUTOR, nullable = false)
    private String autor;

    @Column(name = Constantes.EDITORIAL, nullable = false)
    private String editorial;

    @Column(name = Constantes.ANIO)
    private Integer anio;

    @Column(name = Constantes.ISBN)
    private String isbn;

    @Column(name = Constantes.RESUMEN)
    private String resumen;

    @Column(name = Constantes.GENERO, nullable = false)
    private String genero;

    @Column(name = Constantes.PRECIO, nullable = false)
    private Float precio;

    @Column(name = Constantes.IMAGEN)
    private String imagen;

    @Column(name = Constantes.STOCK, nullable = false)
    private Integer stock;

    @Column(name = Constantes.VISIBLE, nullable = false)
    private Boolean visible;

    @Column(name = Constantes.VALORACION)
    private Integer valoracion;

    public void update(LibroDto libroDto) {
        Optional.ofNullable(libroDto.getTitulo()).ifPresent(titulo -> this.titulo = String.valueOf(titulo));
        Optional.ofNullable(libroDto.getAutor()).ifPresent(autor -> this.autor = String.valueOf(autor));
        Optional.ofNullable(libroDto.getEditorial()).ifPresent(editorial -> this.editorial = String.valueOf(editorial));
        Optional.ofNullable(libroDto.getAnio()).ifPresent(anio -> this.anio = (Integer) anio);
        Optional.ofNullable(libroDto.getIsbn()).ifPresent(isbn -> this.isbn = String.valueOf(isbn));
        Optional.ofNullable(libroDto.getResumen()).ifPresent(resumen -> this.resumen = String.valueOf(resumen));
        Optional.ofNullable(libroDto.getGenero()).ifPresent(genero -> this.genero = String.valueOf(genero));
        Optional.ofNullable(libroDto.getPrecio()).ifPresent(precio -> this.precio = (Float) precio);
        Optional.ofNullable(libroDto.getImagen()).ifPresent(imagen -> this.imagen = String.valueOf(imagen));
        Optional.ofNullable(libroDto.getStock()).ifPresent(stock -> this.stock = (Integer) stock);
        Optional.ofNullable(libroDto.getVisible()).ifPresent(visible -> this.visible = (Boolean) visible);
        Optional.ofNullable(libroDto.getValoracion()).ifPresent(valoracion -> this.valoracion = (Integer) valoracion);
    }
}
