package com.unir.ms_books_catalogue.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.ms_books_catalogue.controller.model.LibroDto;
import com.unir.ms_books_catalogue.data.LibroRepository;
import com.unir.ms_books_catalogue.data.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Libros Service Test")
class LibrosServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LibrosServiceImpl librosService;

    private Libro libroEjemplo;
    private LibroDto libroDtoEjemplo;

    @BeforeEach
    void setUp() {
        libroEjemplo = Libro.builder()
                .id(1L)
                .titulo("El Quijote")
                .autor("Miguel de Cervantes")
                .editorial("Espasa")
                .anio(1605)
                .isbn("978-84-670-1234-5")
                .resumen("Un clásico de la literatura española")
                .genero("Novela")
                .precio(19.99f)
                .stock(100)
                .visible(true)
                .valoracion(5)
                .imagen("quijote.jpg")
                .build();

        libroDtoEjemplo = new LibroDto();
        libroDtoEjemplo.setTitulo("El Quijote");
        libroDtoEjemplo.setAutor("Miguel de Cervantes");
        libroDtoEjemplo.setEditorial("Espasa");
        libroDtoEjemplo.setAnio(1605);
        libroDtoEjemplo.setIsbn("978-84-670-1234-5");
        libroDtoEjemplo.setResumen("Un clásico de la literatura española");
        libroDtoEjemplo.setGenero("Novela");
        libroDtoEjemplo.setPrecio(19.99f);
        libroDtoEjemplo.setStock(100);
        libroDtoEjemplo.setVisible(true);
        libroDtoEjemplo.setValoracion(5);
        libroDtoEjemplo.setImagen("quijote.jpg");
    }

    @Nested
    @DisplayName("Tests para getLibros()")
    class GetLibrosTests {

        @Test
        @DisplayName("getLibros() sin filtros retorna lista de libros")
        void getLibros_sinFiltros_retornaLista() {
            List<Libro> librosEsperados = Arrays.asList(libroEjemplo,
                    Libro.builder().id(2L).titulo("Otro libro").build());
            when(libroRepository.getLibros()).thenReturn(librosEsperados);

            List<Libro> resultado = librosService.getLibros(null, null, null, null, null, null, null);

            assertThat(resultado).isNotNull();
            assertThat(resultado.size()).isEqualTo(2);
            assertThat(resultado).containsExactly(librosEsperados.toArray(new Libro[0]));
            verify(libroRepository).getLibros();
            verify(libroRepository, never()).search(any(), any(), any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("getLibros() con filtros llama a search()")
        void getLibros_conFiltros_llamaASearch() {
            List<Libro> librosEsperados = Collections.singletonList(libroEjemplo);
            when(libroRepository.search(eq("El Quijote"), any(), any(), any(), any(), any(), any()))
                    .thenReturn(librosEsperados);

            List<Libro> resultado = librosService.getLibros("El Quijote", null, null, null, null, null, null);

            assertThat(resultado).isNotNull();
            assertThat(resultado.size()).isEqualTo(1);
            verify(libroRepository).search("El Quijote", null, null, null, null, null, null);
            verify(libroRepository, never()).getLibros();
        }

        @Test
        @DisplayName("getLibros() con lista vacía retorna null")
        void getLibros_listaVacia_retornaNull() {
            when(libroRepository.getLibros()).thenReturn(Collections.emptyList());

            List<Libro> resultado = librosService.getLibros(null, null, null, null, null, null, null);

            assertThat(resultado).isNull();
            verify(libroRepository).getLibros();
        }

        @Test
        @DisplayName("getLibros() con múltiples filtros aplica todos correctamente")
        void getLibros_conMultiplesFiltros_aplicaTodos() {
            List<Libro> librosEsperados = Collections.singletonList(libroEjemplo);
            when(libroRepository.search("El Quijote", "Cervantes", "Espasa", 1605, "clásico", "Novela", true))
                    .thenReturn(librosEsperados);

            List<Libro> resultado = librosService.getLibros("El Quijote", "Cervantes", "Espasa", 1605, "clásico", "Novela", true);

            assertThat(resultado.isEmpty()).isFalse();
            verify(libroRepository).search("El Quijote", "Cervantes", "Espasa", 1605, "clásico", "Novela", true);
        }

        // Test adicional para mejorar cobertura de branches
        @Test
        @DisplayName("getLibros() con un solo filtro de autor")
        void getLibros_conFiltroAutor_llamaASearch() {
            List<Libro> librosEsperados = Collections.singletonList(libroEjemplo);
            when(libroRepository.search(any(), eq("Cervantes"), any(), any(), any(), any(), any()))
                    .thenReturn(librosEsperados);

            List<Libro> resultado = librosService.getLibros(null, "Cervantes", null, null, null, null, null);

            assertThat(resultado).isNotEmpty();
            verify(libroRepository).search(null, "Cervantes", null, null, null, null, null);
        }
        @Test
        @DisplayName("getLibros() con un solo anio")
        void getLibros_soloAnio_llamaSearch() {
            when(libroRepository.search(any(), any(), any(), eq(1605), any(), any(), any()))
                    .thenReturn(List.of(libroEjemplo));

            List<Libro> result = librosService.getLibros(null, null, null, 1605, null, null, null);

            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Tests para crearLibro()")
    class CrearLibroTests {

        @Test
        @DisplayName("crearLibro() con datos válidos retorna libro guardado")
        void crearLibro_datosValidos_retornaLibroGuardado() {
            when(libroRepository.save(any(Libro.class))).thenReturn(libroEjemplo);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);

            ArgumentCaptor<Libro> libroCaptor = ArgumentCaptor.forClass(Libro.class);
            verify(libroRepository).save(libroCaptor.capture());

            Libro libroGuardado = libroCaptor.getValue();
            assertThat(libroGuardado.getTitulo()).isEqualTo("El Quijote");
            assertThat(libroGuardado.getAutor()).isEqualTo("Miguel de Cervantes");
        }

        @Test
        @DisplayName("crearLibro() con libroDto nulo retorna null")
        void crearLibro_dtoNulo_retornaNull() {
            Libro resultado = librosService.crearLibro(null);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con título nulo retorna null")
        void crearLibro_tituloNulo_retornaNull() {
            libroDtoEjemplo.setTitulo(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con título vacío retorna null")
        void crearLibro_tituloVacio_retornaNull() {
            libroDtoEjemplo.setTitulo("   ");

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con autor nulo retorna null")
        void crearLibro_autorNulo_retornaNull() {
            libroDtoEjemplo.setAutor(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con autor vacío retorna null")
        void crearLibro_autorVacio_retornaNull() {
            libroDtoEjemplo.setAutor("   ");

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con editorial nula retorna null")
        void crearLibro_editorialNula_retornaNull() {
            libroDtoEjemplo.setEditorial(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con género nulo retorna null")
        void crearLibro_generoNulo_retornaNull() {
            libroDtoEjemplo.setGenero(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con precio nulo retorna null")
        void crearLibro_precioNulo_retornaNull() {
            libroDtoEjemplo.setPrecio(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con stock nulo retorna null")
        void crearLibro_stockNulo_retornaNull() {
            libroDtoEjemplo.setStock(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("crearLibro() con visible nulo retorna null")
        void crearLibro_visibleNulo_retornaNull() {
            libroDtoEjemplo.setVisible(null);

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }
        @Test
        @DisplayName("crearLibro() caso vacío en crearLibro")
        void crearLibro_generoSoloEspacios_retornaNull() {
            libroDtoEjemplo.setGenero("   ");

            Libro resultado = librosService.crearLibro(libroDtoEjemplo);

            assertThat(resultado).isNull();
        }
    }

    @Nested
    @DisplayName("Tests para getLibro()")
    class GetLibroTests {

        @Test
        @DisplayName("getLibro() con id existente retorna libro")
        void getLibro_idExistente_retornaLibro() {
            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);

            Libro resultado = librosService.getLibro("1");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getTitulo()).isEqualTo("El Quijote");
            verify(libroRepository).getById(1L);
        }

        @Test
        @DisplayName("getLibro() con id no existente retorna null")
        void getLibro_idNoExistente_retornaNull() {
            when(libroRepository.getById(999L)).thenReturn(null);

            Libro resultado = librosService.getLibro("999");

            assertThat(resultado).isNull();
            verify(libroRepository).getById(999L);
        }

        @Test
        @DisplayName("getLibro() con id inválido lanza excepción")
        void getLibro_idInvalido_lanzaExcepcion() {
            assertThatThrownBy(() -> librosService.getLibro("id-invalido"))
                    .isInstanceOf(NumberFormatException.class);
        }
    }

    @Nested
    @DisplayName("Tests para eliminarLibro()")
    class EliminarLibroTests {

        @Test
        @DisplayName("eliminarLibro() con libro existente retorna true")
        void eliminarLibro_existente_retornaTrue() {
            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);
            doNothing().when(libroRepository).delete(libroEjemplo);

            Boolean resultado = librosService.eliminarLibro("1");

            assertThat(resultado).isTrue();
            verify(libroRepository).getById(1L);
            verify(libroRepository).delete(libroEjemplo);
        }

        @Test
        @DisplayName("eliminarLibro() con libro no existente retorna false")
        void eliminarLibro_noExistente_retornaFalse() {
            when(libroRepository.getById(999L)).thenReturn(null);

            Boolean resultado = librosService.eliminarLibro("999");

            assertThat(resultado).isFalse();
            verify(libroRepository).getById(999L);
            verify(libroRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests para actualizarLibro() con PUT")
    class ActualizarLibroPutTests {

        @Test
        @DisplayName("actualizarLibro() PUT exitoso retorna libro actualizado")
        void actualizarLibro_putExitoso_retornaLibroActualizado() {
            LibroDto libroModificado = new LibroDto();
            libroModificado.setTitulo("El Quijote de la Mancha");
            libroModificado.setPrecio(29.99f);
            libroModificado.setStock(75);

            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);
            when(libroRepository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Libro resultado = librosService.actualizarLibro("1", libroModificado);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getTitulo()).isEqualTo("El Quijote de la Mancha");
            assertThat(resultado.getPrecio()).isEqualTo(29.99f);
            assertThat(resultado.getStock()).isEqualTo(75);
            verify(libroRepository).save(any(Libro.class));
        }

        @Test
        @DisplayName("actualizarLibro() PUT con libro no existente retorna null")
        void actualizarLibro_putLibroNoExistente_retornaNull() {
            when(libroRepository.getById(999L)).thenReturn(null);

            Libro resultado = librosService.actualizarLibro("999", libroDtoEjemplo);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("actualizarLibro() PUT con DTO con campos nulos mantiene valores originales")
        void actualizarLibro_putConCamposNulos_mantieneValoresOriginales() {
            LibroDto libroParcial = new LibroDto();
            libroParcial.setPrecio(35.99f);
            // Titulo, autor, etc. quedan null

            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);
            when(libroRepository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Libro resultado = librosService.actualizarLibro("1", libroParcial);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getPrecio()).isEqualTo(35.99f);
            assertThat(resultado.getTitulo()).isEqualTo("El Quijote");
            assertThat(resultado.getAutor()).isEqualTo("Miguel de Cervantes");
        }
    }

    @Nested
    @DisplayName("Tests para actualizarLibro() con PATCH (JSON)")
    class ActualizarLibroPatchTests {

        private final String jsonPatch = "{\"precio\": 24.99, \"stock\": 50}";

        @Test
        @DisplayName("actualizarLibro() PATCH con libro existente y JSON válido")
        void actualizarLibro_patchConJsonValido_procesaCorrectamente() throws Exception {
            ObjectMapper realMapper = new ObjectMapper();

            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);

            doAnswer(invocation -> {
                String json = invocation.getArgument(0);
                return realMapper.readTree(json);
            }).when(objectMapper).readTree(anyString());

            doAnswer(invocation -> {
                Libro libro = invocation.getArgument(0);
                return realMapper.writeValueAsString(libro);
            }).when(objectMapper).writeValueAsString(any(Libro.class));

            doAnswer(invocation -> {
                JsonNode node = invocation.getArgument(0);
                Class<Libro> valueType = invocation.getArgument(1);
                return realMapper.treeToValue(node, valueType);
            }).when(objectMapper).treeToValue(any(JsonNode.class), eq(Libro.class));

            Libro libroActualizadoEsperado = Libro.builder()
                    .id(1L)
                    .titulo("El Quijote")
                    .autor("Miguel de Cervantes")
                    .editorial("Espasa")
                    .anio(1605)
                    .precio(24.99f)
                    .stock(50)
                    .visible(true)
                    .build();

            when(libroRepository.save(any(Libro.class))).thenReturn(libroActualizadoEsperado);

            Libro resultado = librosService.actualizarLibro("1", jsonPatch);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getPrecio()).isEqualTo(24.99f);
            assertThat(resultado.getStock()).isEqualTo(50);
            verify(libroRepository).save(any(Libro.class));
        }

        @Test
        @DisplayName("actualizarLibro() PATCH con libro no existente retorna null")
        void actualizarLibro_patchLibroNoExistente_retornaNull() {
            when(libroRepository.getById(999L)).thenReturn(null);

            Libro resultado = librosService.actualizarLibro("999", jsonPatch);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }

        @Test
        @DisplayName("actualizarLibro() PATCH con JSON inválido retorna null")
        void actualizarLibro_patchJsonInvalido_retornaNull() throws Exception {
            String invalidJson = "{json inválido";

            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);
            when(objectMapper.readTree(invalidJson)).thenThrow(JsonProcessingException.class);

            Libro resultado = librosService.actualizarLibro("1", invalidJson);

            assertThat(resultado).isNull();
            verify(libroRepository, never()).save(any());
        }
        @Test
        void actualizarLibro_patchConError_realFlow() throws Exception {

            when(libroRepository.getById(1L)).thenReturn(libroEjemplo);

            when(objectMapper.readTree(anyString()))
                    .thenThrow(new JsonProcessingException("error") {});

            Libro resultado = librosService.actualizarLibro("1", jsonPatch);

            assertThat(resultado).isNull();
        }
    }
}