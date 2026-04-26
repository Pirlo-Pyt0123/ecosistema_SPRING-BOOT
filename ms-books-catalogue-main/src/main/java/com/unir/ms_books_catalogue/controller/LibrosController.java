package com.unir.ms_books_catalogue.controller;

import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.model.Libro;
import com.unir.ms_books_catalogue.service.LibrosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LibrosController {

    private final LibrosService service;

    @GetMapping("/libros")
    public ResponseEntity<List<Libro>> getLibros(
            @RequestHeader Map<String, String> headers,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editorial,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) String resumen,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Boolean visible) {
        log.info("headers:{}", headers);
        List<Libro> libros = service.getLibros(titulo, autor, editorial, anio, resumen, genero, visible);
        if(libros != null) {
            return ResponseEntity.ok(libros);
        }else{
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/libros/{libroId}")
    public ResponseEntity<Libro> getLibro(@PathVariable String libroId) {

        log.info("Request received for product:{}", libroId);
        Libro libro = service.getLibro(libroId);
        if(libro != null) {
            return ResponseEntity.ok(libro);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/libros/{libroId}")
    public ResponseEntity<Void> deleteLibro(@PathVariable String libroId) {
        Boolean eliminado = service.eliminarLibro(libroId);
        if(eliminado) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/libros")
    public ResponseEntity<Libro> addLibro(@RequestBody LibroDto createBody) {
        Libro libroCreado = service.crearLibro(createBody);
        if(libroCreado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(libroCreado);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/libros/{libroId}")
    public ResponseEntity<Libro> patchLibro(@PathVariable String libroId, @RequestBody String libroActualizar) {
        Libro libroActualizado = service.actualizarLibro(libroId, libroActualizar);
        if(libroActualizado != null) {
            return ResponseEntity.ok(libroActualizado);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/libros/{libroId}")
    public ResponseEntity<Libro> updateLibro(@PathVariable String libroId, @RequestBody LibroDto libroActualizar) {
        Libro libroActualizado = service.actualizarLibro(libroId, libroActualizar);
        if(libroActualizado != null) {
            return ResponseEntity.ok(libroActualizado);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
