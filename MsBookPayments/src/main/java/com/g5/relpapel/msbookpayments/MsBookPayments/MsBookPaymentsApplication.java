package com.g5.relpapel.msbookpayments.MsBookPayments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

/**
 * Clase Principal, se ejecuta al arranque de la aplicacion
 */
@SpringBootApplication
public class MsBookPaymentsApplication {
	public static final String API_NAME = "/api";
	public static void main(String[] args) {
		SpringApplication.run(MsBookPaymentsApplication.class, args);
		System.out.println("  ____              _    _____                                 _       \n" +
				" |  _ \\            | |  |  __ \\                               | |      \n" +
				" | |_) | ___   ___ | | _| |__) |_ _ _   _ _ __ ___   ___ _ __ | |_ ___ \n" +
				" |  _ < / _ \\ / _ \\| |/ /  ___/ _` | | | | '_ ` _ \\ / _ \\ '_ \\| __/ __|\n" +
				" | |_) | (_) | (_) |   <| |  | (_| | |_| | | | | | |  __/ | | | |_\\__ \\\n" +
				" |____/ \\___/ \\___/|_|\\_\\_|   \\__,_|\\__, |_| |_| |_|\\___|_| |_|\\__|___/\n" +
				"                                     __/ |                             \n" +
				"                                    |___/                              ");
		System.out.println("Welcome!, started at "+new Date()+" -> \u00A9 Group 5bol \u2122 FullStack UNIR");
	}

}
