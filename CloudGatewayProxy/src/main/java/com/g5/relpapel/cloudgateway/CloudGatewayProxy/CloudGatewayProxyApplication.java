package com.g5.relpapel.cloudgateway.CloudGatewayProxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudGatewayProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayProxyApplication.class, args);
	}

}
