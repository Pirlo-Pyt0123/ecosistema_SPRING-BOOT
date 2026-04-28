package com.unir.ms_books_catalogue.data.utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SearchStatement Tests")
class SearchStatementTest {
    @Test
    @DisplayName("SearchStatement almacena key correctamente")
    void searchStatement_almacenaKeyCorrectamente() {
        SearchStatement statement = new SearchStatement("titulo", "El Quijote", SearchOperation.EQUAL);

        assertThat(statement.getKey()).isEqualTo("titulo");
    }

    @Test
    @DisplayName("SearchStatement almacena value correctamente")
    void searchStatement_almacenaValueCorrectamente() {
        SearchStatement statement = new SearchStatement("titulo", "El Quijote", SearchOperation.EQUAL);

        assertThat(statement.getValue()).isEqualTo("El Quijote");
    }

    @Test
    @DisplayName("SearchStatement almacena operation correctamente")
    void searchStatement_almacenaOperationCorrectamente() {
        SearchStatement statement = new SearchStatement("titulo", "El Quijote", SearchOperation.EQUAL);

        assertThat(statement.getOperation()).isEqualTo(SearchOperation.EQUAL);
    }

    @Test
    @DisplayName("SearchStatement con operation MATCH")
    void searchStatement_conOperationMatch() {
        SearchStatement statement = new SearchStatement("autor", "Cervantes", SearchOperation.MATCH);

        assertThat(statement.getKey()).isEqualTo("autor");
        assertThat(statement.getValue()).isEqualTo("Cervantes");
        assertThat(statement.getOperation()).isEqualTo(SearchOperation.MATCH);
    }

    @Test
    @DisplayName("SearchStatement con value numérico")
    void searchStatement_conValueNumerico() {
        SearchStatement statement = new SearchStatement("anio", 1605, SearchOperation.EQUAL);

        assertThat(statement.getValue()).isEqualTo(1605);
        assertThat(statement.getOperation()).isEqualTo(SearchOperation.EQUAL);
    }

    @Test
    @DisplayName("SearchStatement con setters funciona correctamente")
    void searchStatement_settersFuncionanCorrectamente() {
        SearchStatement statement = new SearchStatement("titulo", "Libro", SearchOperation.EQUAL);

        statement.setKey("autor");
        statement.setValue("Nuevo Autor");
        statement.setOperation(SearchOperation.MATCH);

        assertThat(statement.getKey()).isEqualTo("autor");
        assertThat(statement.getValue()).isEqualTo("Nuevo Autor");
        assertThat(statement.getOperation()).isEqualTo(SearchOperation.MATCH);
    }
}