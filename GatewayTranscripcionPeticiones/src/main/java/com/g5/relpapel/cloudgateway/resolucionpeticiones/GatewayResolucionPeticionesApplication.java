package com.g5.relpapel.cloudgateway.resolucionpeticiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayResolucionPeticionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayResolucionPeticionesApplication.class, args);
	}

}
