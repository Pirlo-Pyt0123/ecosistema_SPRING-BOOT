package com.g5.relpapel.cloudgateway.CloudGatewayProxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.gateway.discovery.locator.enabled=false"
})
class CloudGatewayProxyApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    // TC-GW-01: El contexto de Spring carga correctamente
    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "El contexto del Gateway debe cargar sin errores");
    }

    // TC-GW-02: La aplicación arranca sin errores y el contexto no es nulo
    @Test
    void applicationContextNoEsNulo() {
        assertNotNull(applicationContext.getId(), "El ID del contexto debe existir");
    }
}