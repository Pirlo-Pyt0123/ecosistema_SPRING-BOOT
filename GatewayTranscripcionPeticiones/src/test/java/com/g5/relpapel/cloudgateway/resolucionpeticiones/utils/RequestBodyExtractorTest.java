package com.g5.relpapel.cloudgateway.resolucionpeticiones.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.relpapel.cloudgateway.resolucionpeticiones.model.GatewayRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para RequestBodyExtractor.
 * Issue #11: [GATEWAY-PRO] Implementar tests para RequestBodyExtractor
 */
class RequestBodyExtractorTest {

    private RequestBodyExtractor extractor;
    private ObjectMapper objectMapper;
    private ServerWebExchange exchange;
    private ServerHttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        extractor = new RequestBodyExtractor(objectMapper);

        // Preparamos un exchange con los headers mínimos
        exchange = mock(ServerWebExchange.class);
        httpRequest = mock(ServerHttpRequest.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.set(HttpHeaders.CONTENT_LENGTH, "50");

        when(exchange.getRequest()).thenReturn(httpRequest);
        when(httpRequest.getHeaders()).thenReturn(headers);
        when(httpRequest.getURI()).thenReturn(URI.create("http://localhost:8080/api/books"));
    }

    /**
     * Caso exitoso: body JSON válido con método GET y queryParams
     */
    @Test
    void getRequest_conJsonValido_debeRetornarGatewayRequest() throws Exception {
        // Arrange — creamos un JSON que representa un GatewayRequest
        String json = """
                {
                  "targetMethod": "GET",
                  "queryParams": {"titulo": ["Clean Code"]},
                  "body": null
                }
                """;
        DataBuffer buffer = buildBuffer(json);

        // Act
        GatewayRequest result = extractor.getRequest(exchange, buffer);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTargetMethod()).isEqualTo(HttpMethod.GET);
        assertThat(result.getExchange()).isEqualTo(exchange);
    }

    /**
     * Caso exitoso: body JSON con método POST y cuerpo
     */
    @Test
    void getRequest_conMetodoPOST_debeRetornarGatewayRequestConBody() throws Exception {
        // Arrange
        String json = """
                {
                  "targetMethod": "POST",
                  "queryParams": {},
                  "body": {"isbn": "978-0134685991", "cantidad": 2}
                }
                """;
        DataBuffer buffer = buildBuffer(json);

        // Act
        GatewayRequest result = extractor.getRequest(exchange, buffer);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTargetMethod()).isEqualTo(HttpMethod.POST);
        assertThat(result.getBody()).isNotNull();
    }

    /**
     * Caso exitoso: verifica que los headers resultantes NO tienen Content-Length
     * y SÍ tienen Transfer-Encoding: chunked (requerimiento del Gateway)
     */
    @Test
    void getRequest_debeEliminarContentLengthYPonerTransferEncodingChunked() throws Exception {
        // Arrange
        String json = """
                {
                  "targetMethod": "GET",
                  "queryParams": {},
                  "body": null
                }
                """;
        DataBuffer buffer = buildBuffer(json);

        // Act
        GatewayRequest result = extractor.getRequest(exchange, buffer);

        // Assert
        HttpHeaders resultHeaders = result.getHeaders();
        assertThat(resultHeaders.containsKey(HttpHeaders.CONTENT_LENGTH)).isFalse();
        assertThat(resultHeaders.getFirst(HttpHeaders.TRANSFER_ENCODING)).isEqualTo("chunked");
    }

    /**
     * Caso de error: JSON inválido debe lanzar excepción
     */
    @Test
    void getRequest_conJsonInvalido_debeLanzarExcepcion() {
        // Arrange
        String invalidJson = "esto no es JSON valido !!!";
        DataBuffer buffer = buildBuffer(invalidJson);

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            extractor.getRequest(exchange, buffer);
        });
    }

    /**
     * Caso de error: body vacío debe lanzar excepción
     */
    @Test
    void getRequest_conBodyVacio_debeLanzarExcepcion() {
        // Arrange
        DataBuffer buffer = buildBuffer("");

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            extractor.getRequest(exchange, buffer);
        });
    }

    // ========================
    //  Método auxiliar
    // ========================

    /**
     * Construye un DataBuffer a partir de un String (simula el body de la petición HTTP)
     */
    private DataBuffer buildBuffer(String content) {
        DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
        byte[] bytes = content.getBytes();
        DataBuffer buffer = factory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }
}
