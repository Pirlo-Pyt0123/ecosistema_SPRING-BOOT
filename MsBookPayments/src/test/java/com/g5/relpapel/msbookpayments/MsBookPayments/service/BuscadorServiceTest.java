package com.g5.relpapel.msbookpayments.MsBookPayments.service;

import com.g5.relpapel.msbookpayments.MsBookPayments.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para BuscadorService.
 * Issue #10: [PAYMENTS] Implementar tests unitarios para BuscadorService
 *
 * Nota: se usa Mockito para simular las llamadas HTTP al microservicio Catálogo.
 * En ningún caso se hace una petición real a la red ni a base de datos.
 */
@ExtendWith(MockitoExtension.class)
class BuscadorServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BuscadorService buscadorService;

    private static final String BASE_URL = "http://MSBOOKSCATALOGUE/api/books/";
    private static final String LIBRO_ID = "libro-123";

    @BeforeEach
    void setUp() {
        // Inyectamos la URL via ReflectionTestUtils porque viene de @Value
        ReflectionTestUtils.setField(buscadorService, "buscadorUrl", BASE_URL);
    }

    // ========================
    //  Tests de validarItem()
    // ========================

    @Test
    void validarItem_cuandoLibroExisteYHayStock_debeRetornarItem() {
        // Arrange — simulamos que el catálogo devuelve un libro visible con stock
        Item itemMock = new Item(LIBRO_ID, "Clean Code", "978-0132350884", 10, true);
        ResponseEntity<Item> responseMock = new ResponseEntity<>(itemMock, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.GET),
                isNull(),
                eq(Item.class)
        )).thenReturn(responseMock);

        // Act
        Item resultado = buscadorService.validarItem(LIBRO_ID, 3);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(LIBRO_ID);
        assertThat(resultado.getTitulo()).isEqualTo("Clean Code");
    }

    @Test
    void validarItem_cuandoStockEsInsuficiente_debeRetornarNull() {
        // Arrange — solo hay 2 libros pero pedimos 5
        Item itemMock = new Item(LIBRO_ID, "Clean Code", "978-0132350884", 2, true);
        ResponseEntity<Item> responseMock = new ResponseEntity<>(itemMock, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.GET),
                isNull(),
                eq(Item.class)
        )).thenReturn(responseMock);

        // Act
        Item resultado = buscadorService.validarItem(LIBRO_ID, 5);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    void validarItem_cuandoLibroNoEstaVisible_debeRetornarNull() {
        // Arrange — el libro existe pero está oculto (visible = false)
        Item itemMock = new Item(LIBRO_ID, "Libro Oculto", "978-0000000000", 100, false);
        ResponseEntity<Item> responseMock = new ResponseEntity<>(itemMock, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.GET),
                isNull(),
                eq(Item.class)
        )).thenReturn(responseMock);

        // Act
        Item resultado = buscadorService.validarItem(LIBRO_ID, 1);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    void validarItem_cuandoElCatalogoDevuelve404_debeRetornarNull() {
        // Arrange — el microservicio catálogo responde con 404 Not Found
        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.GET),
                isNull(),
                eq(Item.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act
        Item resultado = buscadorService.validarItem(LIBRO_ID, 1);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    void validarItem_cuandoElCatalogoDevuelveError500_debeRetornarNull() {
        // Arrange — el microservicio catálogo tiene un error interno
        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.GET),
                isNull(),
                eq(Item.class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        Item resultado = buscadorService.validarItem(LIBRO_ID, 1);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    void validarItem_cuandoOcurreExcepcionInesperada_debeRetornarNull() {
        // Arrange — simula un timeout u otro error de red
        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.GET),
                isNull(),
                eq(Item.class)
        )).thenThrow(new RuntimeException("Connection timed out"));

        // Act
        Item resultado = buscadorService.validarItem(LIBRO_ID, 1);

        // Assert
        assertThat(resultado).isNull();
    }

    // ========================
    //  Tests de notificarCompra()
    // ========================

    @Test
    void notificarCompra_cuandoHayStockSuficiente_debActualizarStockCorrectamente() throws Exception {
        // Arrange
        Item libro = new Item(LIBRO_ID, "Clean Code", "978-0132350884", 10, true);
        Item libroActualizado = new Item(LIBRO_ID, "Clean Code", "978-0132350884", 7, true);
        ResponseEntity<Item> patchResponse = new ResponseEntity<>(libroActualizado, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Item.class)
        )).thenReturn(patchResponse);

        // Act & Assert — no debe lanzar excepción
        buscadorService.notificarCompra(libro, 3);

        verify(restTemplate, times(1)).exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Item.class)
        );
    }

    @Test
    void notificarCompra_cuandoLibroEsNull_debeLanzarExcepcion() {
        // Act & Assert
        assertThrows(Exception.class, () -> buscadorService.notificarCompra(null, 1));
    }

    @Test
    void notificarCompra_cuandoStockInsuficiente_debeLanzarExcepcion() {
        // Arrange — solo hay 2 libros pero intentamos comprar 5
        Item libro = new Item(LIBRO_ID, "Clean Code", "978-0132350884", 2, true);

        // Act & Assert
        assertThrows(Exception.class, () -> buscadorService.notificarCompra(libro, 5));
    }

    @Test
    void notificarCompra_cuandoElPatchFalla_debeLanzarExcepcion() {
        // Arrange — el PATCH devuelve un código que no es 200
        Item libro = new Item(LIBRO_ID, "Clean Code", "978-0132350884", 10, true);
        ResponseEntity<Item> patchFallidoResponse = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(
                eq(BASE_URL + LIBRO_ID),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Item.class)
        )).thenReturn(patchFallidoResponse);

        // Act & Assert
        assertThrows(Exception.class, () -> buscadorService.notificarCompra(libro, 3));
    }
}
