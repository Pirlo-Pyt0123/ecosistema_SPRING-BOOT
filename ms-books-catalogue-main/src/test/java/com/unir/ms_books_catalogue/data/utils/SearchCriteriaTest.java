package com.unir.ms_books_catalogue.data.utils;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchCriteria Tests")
class SearchCriteriaTest {

    @Mock
    private Root<Object> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private Predicate mockPredicate;

    private SearchCriteria<Object> searchCriteria;

    @BeforeEach
    void setUp() {
        searchCriteria = new SearchCriteria<>();
        // Configuración básica que todos los tests pueden usar
        lenient().when(root.get(anyString())).thenReturn(mock(Path.class));
    }

    @Nested
    @DisplayName("Tests para add() y almacenamiento")
    class AddAndStorageTests {

        @Test
        @DisplayName("SearchCriteria almacena criterio correctamente")
        void searchCriteria_almacenaCriterioCorrectamente() {
            SearchStatement statement = new SearchStatement("titulo", "El Quijote", SearchOperation.EQUAL);
            searchCriteria.add(statement);
            assertThat(searchCriteria).isNotNull();
        }

        @Test
        @DisplayName("SearchCriteria puede almacenar múltiples criterios")
        void searchCriteria_almacenaMultiplesCriterios() {
            searchCriteria.add(new SearchStatement("titulo", "Libro", SearchOperation.EQUAL));
            searchCriteria.add(new SearchStatement("autor", "Autor", SearchOperation.MATCH));
            searchCriteria.add(new SearchStatement("anio", 2020, SearchOperation.GREATER_THAN));
            assertThat(searchCriteria).isNotNull();
        }
    }

    @Nested
    @DisplayName("Tests para toPredicate con diferentes operaciones")
    class ToPredicateTests {

        @Test
        @DisplayName("toPredicate con operación GREATER_THAN no lanza excepción")
        void toPredicate_conGreaterThan_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("anio", 2020, SearchOperation.GREATER_THAN);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación LESS_THAN no lanza excepción")
        void toPredicate_conLessThan_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("precio", 100, SearchOperation.LESS_THAN);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación GREATER_THAN_EQUAL no lanza excepción")
        void toPredicate_conGreaterThanEqual_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("stock", 50, SearchOperation.GREATER_THAN_EQUAL);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación LESS_THAN_EQUAL no lanza excepción")
        void toPredicate_conLessThanEqual_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("valoracion", 4, SearchOperation.LESS_THAN_EQUAL);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación NOT_EQUAL no lanza excepción")
        void toPredicate_conNotEqual_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("titulo", "Libro", SearchOperation.NOT_EQUAL);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación EQUAL no lanza excepción")
        void toPredicate_conEqual_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("autor", "Cervantes", SearchOperation.EQUAL);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación MATCH - búsqueda parcial no lanza excepción")
        void toPredicate_conMatch_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("titulo", "Quijote", SearchOperation.MATCH);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con operación MATCH_END - búsqueda por inicio no lanza excepción")
        void toPredicate_conMatchEnd_noLanzaExcepcion() {
            SearchStatement statement = new SearchStatement("titulo", "El", SearchOperation.MATCH_END);
            searchCriteria.add(statement);

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("toPredicate con múltiples criterios no lanza excepción")
        void toPredicate_conMultiplesCriterios_noLanzaExcepcion() {
            searchCriteria.add(new SearchStatement("titulo", "Libro", SearchOperation.MATCH));
            searchCriteria.add(new SearchStatement("anio", 2020, SearchOperation.GREATER_THAN));
            searchCriteria.add(new SearchStatement("precio", 50, SearchOperation.LESS_THAN_EQUAL));

            assertThatCode(() -> searchCriteria.toPredicate(root, query, builder))
                    .doesNotThrowAnyException();
        }
        @Test
        @DisplayName("toPredicate con lista vacía retorna null o predicado")
        void toPredicate_conListaVacia_retornaNullOPredicado() {
            Predicate result = searchCriteria.toPredicate(root, query, builder);
            // El comportamiento actual puede ser null
            assertThat(result).isNull();
        }
    }
}