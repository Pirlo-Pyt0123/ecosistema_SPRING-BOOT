package com.g5.relpapel.eurekaserverapp.EurekaServerApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
class EurekaServerAppApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    // TC-EUR-01: El contexto de Spring carga correctamente
    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "El contexto de Spring debe cargar sin errores");
    }

    // TC-EUR-02: La aplicación arranca sin errores y el contexto no es nulo
    @Test
    void applicationContextNoEsNulo() {
        assertNotNull(applicationContext.getId(), "El ID del contexto debe existir");
    }
}