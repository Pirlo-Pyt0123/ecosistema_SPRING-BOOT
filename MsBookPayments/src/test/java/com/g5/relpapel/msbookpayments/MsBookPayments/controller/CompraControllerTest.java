package com.g5.relpapel.msbookpayments.MsBookPayments.controller;

import com.g5.relpapel.msbookpayments.MsBookPayments.model.Compra;
import com.g5.relpapel.msbookpayments.MsBookPayments.model.Item;
import com.g5.relpapel.msbookpayments.MsBookPayments.repository.CompraRepository;
import com.g5.relpapel.msbookpayments.MsBookPayments.service.BuscadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompraController Tests")
class CompraControllerTest {

    @Mock
    private BuscadorService buscadorService;

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraController compraController;

    private Compra compraValida;
    private Item itemValido;

    @BeforeEach
    void setUp() {
        compraValida = new Compra();
        compraValida.setLibroId("libro-123");
        compraValida.setCantidad(2);
        compraValida.setPrecioTotal(39.98);

        itemValido = new Item("libro-123", "El Quijote", "978-84-670-1234-5", 10, true);
    }

    @Nested
    @DisplayName("Tests para POST /compras - registrarCompra()")
    class RegistrarCompraTests {

        @Test
        @DisplayName("POST /compras con item válido retorna 201 Created")
        void registrarCompra_conItemValido_retorna201Created() throws Exception {
            // Given
            when(buscadorService.validarItem(compraValida.getLibroId(), compraValida.getCantidad()))
                    .thenReturn(itemValido);
            doNothing().when(buscadorService).notificarCompra(any(Item.class), eq(compraValida.getCantidad()));
            when(compraRepository.save(any(Compra.class))).thenReturn(compraValida);

            // When
            ResponseEntity<String> response = compraController.registrarCompra(compraValida);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo("Compra registrada con éxito");
        }

        @Test
        @DisplayName("POST /compras con item válido guarda en repositorio")
        void registrarCompra_conItemValido_guardaEnRepositorio() throws Exception {
            // Given
            when(buscadorService.validarItem(compraValida.getLibroId(), compraValida.getCantidad()))
                    .thenReturn(itemValido);
            doNothing().when(buscadorService).notificarCompra(any(Item.class), eq(compraValida.getCantidad()));
            when(compraRepository.save(any(Compra.class))).thenReturn(compraValida);

            // When
            compraController.registrarCompra(compraValida);

            // Then
            ArgumentCaptor<Compra> compraCaptor = ArgumentCaptor.forClass(Compra.class);
            verify(compraRepository).save(compraCaptor.capture());

            Compra compraGuardada = compraCaptor.getValue();
            assertThat(compraGuardada.getLibroId()).isEqualTo("libro-123");
            assertThat(compraGuardada.getCantidad()).isEqualTo(2);
            assertThat(compraGuardada.getPrecioTotal()).isEqualTo(39.98);
        }

        @Test
        @DisplayName("POST /compras con item válido notifica al servicio")
        void registrarCompra_conItemValido_notificaAlServicio() throws Exception {
            // Given
            when(buscadorService.validarItem(compraValida.getLibroId(), compraValida.getCantidad()))
                    .thenReturn(itemValido);
            doNothing().when(buscadorService).notificarCompra(any(Item.class), eq(compraValida.getCantidad()));
            when(compraRepository.save(any(Compra.class))).thenReturn(compraValida);

            // When
            compraController.registrarCompra(compraValida);

            // Then
            ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
            verify(buscadorService).notificarCompra(itemCaptor.capture(), eq(2));

            Item itemNotificado = itemCaptor.getValue();
            assertThat(itemNotificado.getId()).isEqualTo("libro-123");
            assertThat(itemNotificado.getTitulo()).isEqualTo("El Quijote");
        }

        @Test
        @DisplayName("POST /compras con BuscadorService retorna null → 400 Bad Request")
        void registrarCompra_cuandoBuscadorServiceRetornaNull_retorna400BadRequest() throws Exception {
            // Given
            when(buscadorService.validarItem(compraValida.getLibroId(), compraValida.getCantidad()))
                    .thenReturn(null);

            // When
            ResponseEntity<String> response = compraController.registrarCompra(compraValida);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isEqualTo("Item no válido, no visible o fuera de stock");

            verify(compraRepository, never()).save(any(Compra.class));
            verify(buscadorService, never()).notificarCompra(any(Item.class), anyInt());
        }

        @Test
        @DisplayName("POST /compras con item inválido (stock insuficiente) retorna 400")
        void registrarCompra_conItemInvalidoStockInsuficiente_retorna400BadRequest() throws Exception {
            // Given
            when(buscadorService.validarItem(compraValida.getLibroId(), compraValida.getCantidad()))
                    .thenReturn(null); // BuscadorService retorna null para items inválidos

            // When
            ResponseEntity<String> response = compraController.registrarCompra(compraValida);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isEqualTo("Item no válido, no visible o fuera de stock");

            verify(compraRepository, never()).save(any(Compra.class));
            verify(buscadorService, never()).notificarCompra(any(Item.class), anyInt());
        }

        @Test
        @DisplayName("POST /compras con libroId nulo retorna 400")
        void registrarCompra_conLibroIdNulo_retorna400BadRequest() throws Exception {
            // Given
            Compra compraInvalida = new Compra();
            compraInvalida.setLibroId(null);
            compraInvalida.setCantidad(1);

            when(buscadorService.validarItem(null, 1)).thenReturn(null);

            // When
            ResponseEntity<String> response = compraController.registrarCompra(compraInvalida);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isEqualTo("Item no válido, no visible o fuera de stock");

            verify(compraRepository, never()).save(any(Compra.class));
        }

        @Test
        @DisplayName("POST /compras con cantidad negativa retorna 400")
        void registrarCompra_conCantidadNegativa_retorna400BadRequest() throws Exception {
            // Given
            Compra compraInvalida = new Compra();
            compraInvalida.setLibroId("libro-123");
            compraInvalida.setCantidad(-5);

            when(buscadorService.validarItem("libro-123", -5)).thenReturn(null);

            // When
            ResponseEntity<String> response = compraController.registrarCompra(compraInvalida);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isEqualTo("Item no válido, no visible o fuera de stock");

            verify(compraRepository, never()).save(any(Compra.class));
        }
    }
}