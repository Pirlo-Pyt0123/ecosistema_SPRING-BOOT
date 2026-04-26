package com.unir.ms_books_catalogue.data.model;

import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.utils.Constantes;
import jakarta.persistence.*;
import lombok.*;

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

    public void update(LibroDto libroDto){
        this.titulo = libroDto.getTitulo();
        this.autor = libroDto.getAutor();
        this.editorial = libroDto.getEditorial();
        this.anio = libroDto.getAnio();
        this.isbn = libroDto.getIsbn();
        this.resumen = libroDto.getResumen();
        this.genero = libroDto.getGenero();
        this.precio = libroDto.getPrecio();
        this.imagen = libroDto.getImagen();
        this.stock = libroDto.getStock();
        this.visible = libroDto.getVisible();
        this.valoracion = libroDto.getValoracion();
    }

}
