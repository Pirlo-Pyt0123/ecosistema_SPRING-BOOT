package com.unir.ms_books_catalogue.data.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Constantes Tests")
class ConstantesTest {
    @Test
    @DisplayName("Constantes tienen los valores esperados")
    void constantes_tienenValoresEsperados() {
        assertThat(Constantes.TITULO).isEqualTo("titulo");
        assertThat(Constantes.AUTOR).isEqualTo("autor");
        assertThat(Constantes.EDITORIAL).isEqualTo("editorial");
        assertThat(Constantes.ANIO).isEqualTo("anio");
        assertThat(Constantes.ISBN).isEqualTo("isbn");
        assertThat(Constantes.RESUMEN).isEqualTo("resumen");
        assertThat(Constantes.GENERO).isEqualTo("genero");
        assertThat(Constantes.PRECIO).isEqualTo("precio");
        assertThat(Constantes.IMAGEN).isEqualTo("imagen");
        assertThat(Constantes.STOCK).isEqualTo("stock");
        assertThat(Constantes.VISIBLE).isEqualTo("visible");
        assertThat(Constantes.VALORACION).isEqualTo("valoracion");
    }

    @Test
    @DisplayName("Constantes no son null")
    void constantes_noSonNull() {
        assertThat(Constantes.TITULO).isNotNull();
        assertThat(Constantes.AUTOR).isNotNull();
        assertThat(Constantes.EDITORIAL).isNotNull();
        assertThat(Constantes.ANIO).isNotNull();
        assertThat(Constantes.ISBN).isNotNull();
        assertThat(Constantes.RESUMEN).isNotNull();
        assertThat(Constantes.GENERO).isNotNull();
        assertThat(Constantes.PRECIO).isNotNull();
        assertThat(Constantes.IMAGEN).isNotNull();
        assertThat(Constantes.STOCK).isNotNull();
        assertThat(Constantes.VISIBLE).isNotNull();
        assertThat(Constantes.VALORACION).isNotNull();
    }
    @Test
    void constructor_privado_lanzaExcepcion() throws Exception {
        var constructor = Constantes.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var exception = assertThrows(Exception.class, constructor::newInstance);

        assertThat(exception.getCause()).isInstanceOf(UnsupportedOperationException.class);
    }
}