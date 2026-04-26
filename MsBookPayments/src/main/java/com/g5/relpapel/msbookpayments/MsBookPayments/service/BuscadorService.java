package com.g5.relpapel.msbookpayments.MsBookPayments.service;

import com.g5.relpapel.msbookpayments.MsBookPayments.model.Item;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactua con otro microservicio para buscar libros
 */
@Service
@Slf4j
public class BuscadorService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${app.microservice.endpoint.searchBooks:http://MSBOOKSCATALOGUE/api/books}")
    private String buscadorUrl;

    @PostConstruct
    public void init() {
        //solo para debug
        System.out.println("URL del Microservicio Buscador: " + buscadorUrl);
    }

    /**
     * Buscar el libro que se desea comprar para consultar si está visible y si su stock satisface la compra
     *
     * @param itemId             Identificador del libro a comprar
     * @param cantidadSolicitada Unidades del libro que se comprará
     * @return Datos del libro que desea comprar. Null si no existe o si no esta visible o si el stock no alcanza la cantidad solicitada
     */
    public Item validarItem(String itemId, int cantidadSolicitada) {
        try {
            ResponseEntity<Item> response = restTemplate.exchange(
                    buscadorUrl + itemId,
                    HttpMethod.GET,
                    null,
                    Item.class); //se espera la respuesta en un objeto del tipo Item.class
            Item itemResponse = response.getBody();
            return (response.getStatusCode() == HttpStatus.OK &&
                    itemResponse.isStockDisponible(cantidadSolicitada)) ? itemResponse : null;
        } catch (HttpClientErrorException e) {
            log.error("Client Error: {}, Libro ID: {}", e.getStatusCode(), itemId);
            return null;
        } catch (HttpServerErrorException e) {
            log.error("Server Error: {}, Libro ID: {}", e.getStatusCode(), itemId);
            return null;
        } catch (Exception e) {
            log.error("Error: {}, Libro ID: {}", e.getMessage(), itemId);
            return null;
        }
    }

    /**
     * Notifica la compra reduciendo el stock en el microservicio que proporciona datos del libro
     *
     * @param libro    Datos del Libro
     * @param cantidad Cantidad solicitada
     * @throws Exception Lanzamos una excepción para deshacer el registro de la compra en la base de datos
     */
    public void notificarCompra(Item libro, int cantidad) throws Exception {
        if (libro != null && libro.getStock() >= cantidad) {
            int nuevoStock = libro.getStock() - cantidad;
            String jsonPatch = "{ \"stock\": " + nuevoStock + " }";
            ResponseEntity<Item> updateResponse = restTemplate.exchange(
                    buscadorUrl + libro.getId(),
                    HttpMethod.PATCH,
                    new HttpEntity<>(jsonPatch, createJsonHeaders()),
                    Item.class);
            if (updateResponse.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Imposible completar el registro de compra");
            }
        } else
            throw new Exception("No se pudo completar el registro de la compra");
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

