package com.unir.ms_books_catalogue.data;

import com.unir.ms_books_catalogue.data.model.Libro;
import com.unir.ms_books_catalogue.data.utils.Constantes;
import com.unir.ms_books_catalogue.data.utils.SearchCriteria;
import com.unir.ms_books_catalogue.data.utils.SearchOperation;
import com.unir.ms_books_catalogue.data.utils.SearchStatement;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LibroRepository {

    private final LibroJpaRepository repository;

    public List<Libro> getLibros(){ return repository.findAll(); }

    public Libro getById(Long id){ return repository.findById(id).orElse(null); }

    public Libro save(Libro libro){ return repository.save(libro); }

    public void delete(Libro libro){ repository.delete(libro); }

    public List<Libro> search(String titulo, String autor, String editorial, Integer anio, String resumen, String genero, Boolean visible){
        SearchCriteria<Libro> spec = new SearchCriteria<>();

        if(StringUtils.isNotBlank(titulo)){
            spec.add(new SearchStatement(Constantes.TITULO, titulo, SearchOperation.MATCH));
        }

        if(StringUtils.isNotBlank(autor)){
            spec.add(new SearchStatement(Constantes.AUTOR, autor, SearchOperation.MATCH));
        }

        if(StringUtils.isNotBlank(editorial)){
            spec.add(new SearchStatement(Constantes.EDITORIAL, editorial, SearchOperation.MATCH));
        }

        if(anio != null){
            spec.add(new SearchStatement(Constantes.ANIO, anio, SearchOperation.EQUAL));
        }

        if(StringUtils.isNotBlank(resumen)){
            spec.add(new SearchStatement(Constantes.RESUMEN, resumen, SearchOperation.MATCH));
        }

        if(StringUtils.isNotBlank(genero)){
            spec.add(new SearchStatement(Constantes.GENERO, genero, SearchOperation.EQUAL));
        }

        if(visible != null){
            spec.add(new SearchStatement(Constantes.VISIBLE, visible, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }
}
