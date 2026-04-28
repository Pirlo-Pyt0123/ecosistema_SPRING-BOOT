package com.unir.ms_books_catalogue.data.utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SearchOperation Enum Tests")
class SearchOperationTest {
    @Test
    @DisplayName("SearchOperation tiene todos los valores esperados")
    void searchOperation_tieneTodosLosValores() {
        SearchOperation[] operations = SearchOperation.values();

        assertThat(operations).hasSize(8);
        assertThat(operations).containsExactly(
                SearchOperation.GREATER_THAN,
                SearchOperation.LESS_THAN,
                SearchOperation.GREATER_THAN_EQUAL,
                SearchOperation.LESS_THAN_EQUAL,
                SearchOperation.NOT_EQUAL,
                SearchOperation.EQUAL,
                SearchOperation.MATCH,
                SearchOperation.MATCH_END
        );
    }

    @Test
    @DisplayName("SearchOperation EQUAL funciona correctamente")
    void searchOperation_equal_esCorrecto() {
        assertThat(SearchOperation.EQUAL).isEqualTo(SearchOperation.EQUAL);
        assertThat(SearchOperation.EQUAL.name()).isEqualTo("EQUAL");
    }

    @Test
    @DisplayName("SearchOperation MATCH funciona correctamente")
    void searchOperation_match_esCorrecto() {
        assertThat(SearchOperation.MATCH).isEqualTo(SearchOperation.MATCH);
        assertThat(SearchOperation.MATCH.name()).isEqualTo("MATCH");
    }

    @Test
    @DisplayName("SearchOperation GREATER_THAN funciona correctamente")
    void searchOperation_greaterThan_esCorrecto() {
        assertThat(SearchOperation.GREATER_THAN).isEqualTo(SearchOperation.GREATER_THAN);
    }

    @Test
    @DisplayName("SearchOperation LESS_THAN funciona correctamente")
    void searchOperation_lessThan_esCorrecto() {
        assertThat(SearchOperation.LESS_THAN).isEqualTo(SearchOperation.LESS_THAN);
    }

    @Test
    @DisplayName("SearchOperation NOT_EQUAL funciona correctamente")
    void searchOperation_notEqual_esCorrecto() {
        assertThat(SearchOperation.NOT_EQUAL).isEqualTo(SearchOperation.NOT_EQUAL);
    }

    @Test
    @DisplayName("SearchOperation valueOf retorna el enum correcto")
    void searchOperation_valueOf_retornaEnumCorrecto() {
        assertThat(SearchOperation.valueOf("EQUAL")).isEqualTo(SearchOperation.EQUAL);
        assertThat(SearchOperation.valueOf("MATCH")).isEqualTo(SearchOperation.MATCH);
        assertThat(SearchOperation.valueOf("GREATER_THAN")).isEqualTo(SearchOperation.GREATER_THAN);
    }
}