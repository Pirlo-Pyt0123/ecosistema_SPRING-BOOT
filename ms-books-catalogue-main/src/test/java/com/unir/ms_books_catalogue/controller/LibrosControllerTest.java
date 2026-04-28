package com.unir.ms_books_catalogue.controller;

import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.model.Libro;
import com.unir.ms_books_catalogue.service.LibrosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibrosController.class)
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
class LibrosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibrosService service;

    private static final String LIBRO_JSON_VALIDO = """
            {
              "titulo": "El Quijote",
              "autor": "Miguel de Cervantes",
              "editorial": "Anaya",
              "anio": 1605,
              "isbn": "978-84-667",
              "resumen": "Resumen de prueba",
              "genero": "Novela",
              "precio": 19.99,
              "stock": 10,
              "visible": true
            }
            """;

    private static final String LIBRO_JSON_INVALIDO = """
            {
              "titulo": "",
              "autor": "",
              "editorial": "",
              "genero": "",
              "precio": null,
              "stock": null,
              "visible": null
            }
            """;

    private Libro libroEjemplo() {
        return Libro.builder()
                .id(1L)
                .titulo("El Quijote")
                .autor("Miguel de Cervantes")
                .editorial("Anaya")
                .genero("Novela")
                .precio(19.99f)
                .stock(10)
                .visible(true)
                .build();
    }

    // TC-01: GET /libros retorna lista con 200 OK
    @Test
    void getLibros_retornaLista_200() throws Exception {
        when(service.getLibros(null, null, null, null, null, null, null))
                .thenReturn(List.of(libroEjemplo()));

        mockMvc.perform(get("/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("El Quijote"))
                .andExpect(jsonPath("$[0].autor").value("Miguel de Cervantes"));
    }

    // TC-02: GET /libros cuando el servicio retorna null devuelve lista vacía
    @Test
    void getLibros_listaVacia_retornaVacio() throws Exception {
        when(service.getLibros(null, null, null, null, null, null, null))
                .thenReturn(null);

        mockMvc.perform(get("/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // TC-03: GET /libros/{id} libro existente retorna 200 OK
    @Test
    void getLibro_existente_200() throws Exception {
        when(service.getLibro("1")).thenReturn(libroEjemplo());

        mockMvc.perform(get("/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    // TC-04: GET /libros/{id} libro no encontrado retorna 404
    @Test
    void getLibro_noExistente_404() throws Exception {
        when(service.getLibro("9999")).thenReturn(null);

        mockMvc.perform(get("/libros/9999"))
                .andExpect(status().isNotFound());
    }

    // TC-05: POST /libros datos válidos retorna 201 Created
    @Test
    void addLibro_datosValidos_201() throws Exception {
        when(service.crearLibro(any(LibroDto.class))).thenReturn(libroEjemplo());

        mockMvc.perform(post("/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LIBRO_JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    // TC-06: POST /libros cuando el servicio retorna null (datos inválidos) retorna 400
    @Test
    void addLibro_datosInvalidos_400() throws Exception {
        when(service.crearLibro(any(LibroDto.class))).thenReturn(null);

        mockMvc.perform(post("/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LIBRO_JSON_INVALIDO))
                .andExpect(status().isBadRequest());
    }

    // TC-07: DELETE /libros/{id} existente retorna 200 OK
    @Test
    void deleteLibro_existente_200() throws Exception {
        when(service.eliminarLibro("1")).thenReturn(true);

        mockMvc.perform(delete("/libros/1"))
                .andExpect(status().isOk());
    }

    // TC-08: DELETE /libros/{id} no existente retorna 404
    @Test
    void deleteLibro_noExistente_404() throws Exception {
        when(service.eliminarLibro("9999")).thenReturn(false);

        mockMvc.perform(delete("/libros/9999"))
                .andExpect(status().isNotFound());
    }

    // TC-09: PATCH /libros/{id} actualización parcial retorna 200 OK
    @Test
    void patchLibro_exitoso_200() throws Exception {
        when(service.actualizarLibro(eq("1"), anyString())).thenReturn(libroEjemplo());

        mockMvc.perform(patch("/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    // TC-10: PUT /libros/{id} actualización completa retorna 200 OK
    @Test
    void putLibro_exitoso_200() throws Exception {
        when(service.actualizarLibro(eq("1"), any(LibroDto.class))).thenReturn(libroEjemplo());

        mockMvc.perform(put("/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LIBRO_JSON_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }
}