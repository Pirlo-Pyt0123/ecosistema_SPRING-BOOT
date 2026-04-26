package com.unir.ms_books_catalogue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.LibroRepository;
import com.unir.ms_books_catalogue.data.model.Libro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibrosServiceImpl implements LibrosService {

    private final LibroRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public List<Libro> getLibros(String titulo, String autor, String editorial, Integer anio, String resumen, String genero, Boolean visible) {
        if (StringUtils.hasLength(titulo) || StringUtils.hasLength(autor) || StringUtils.hasLength(editorial) || anio != null || StringUtils.hasLength(resumen) || StringUtils.hasLength(genero) || visible != null) {
            return repository.search(titulo, autor, editorial, anio, resumen, genero, visible);
        }
        List<Libro> libros = repository.getLibros();
        return libros.isEmpty() ? null : libros;
    }

    @Override
    public Libro getLibro(String id) {
        return repository.getById(Long.valueOf(id));
    }

    @Override
    public Boolean eliminarLibro(String id) {
        Libro libro = repository.getById(Long.valueOf(id));
        if (libro != null) {
            repository.delete(libro);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Libro crearLibro(LibroDto libroCrear) {
        if (libroCrear != null && StringUtils.hasLength(libroCrear.getTitulo().trim())
                && StringUtils.hasLength(libroCrear.getAutor().trim())
                && StringUtils.hasLength(libroCrear.getEditorial().trim())
                && StringUtils.hasLength(libroCrear.getGenero().trim())
                && libroCrear.getPrecio() != null
                && libroCrear.getStock() != null
                && libroCrear.getVisible() != null) {
            Libro libro = Libro.builder()
                    .titulo(libroCrear.getTitulo())
                    .autor(libroCrear.getAutor())
                    .editorial(libroCrear.getEditorial())
                    .anio(libroCrear.getAnio())
                    .isbn(libroCrear.getIsbn())
                    .resumen(libroCrear.getResumen())
                    .genero(libroCrear.getGenero())
                    .precio(libroCrear.getPrecio())
                    .imagen(libroCrear.getImagen())
                    .stock(libroCrear.getStock())
                    .visible(libroCrear.getVisible())
                    .build();
            return repository.save(libro);
        } else {
            return null;
        }
    }

    @Override
    public Libro actualizarLibro(String id, String libroModificar) {
        Libro libro = repository.getById(Long.valueOf(id));
        if (libro != null) {
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(libroModificar));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(libro)));
                Libro libroActualizado = objectMapper.treeToValue(target, Libro.class);
                repository.save(libroActualizado);
                return libroActualizado;
            } catch (JsonProcessingException | JsonPatchException e) {
                log.error("Error al actualizar el libro {}", id, e);
            }
        }
        return null;
    }

    @Override
    public Libro actualizarLibro(String id, LibroDto libroModificar) {
        Libro libro = repository.getById(Long.valueOf(id));
        if (libro != null) {
            libro.update(libroModificar);
            repository.save(libro);
            return libro;
        } else {
            return null;
        }
    }
}
