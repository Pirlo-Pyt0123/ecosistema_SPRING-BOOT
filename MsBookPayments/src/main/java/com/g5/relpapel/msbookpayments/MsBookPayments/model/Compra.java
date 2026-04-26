package com.g5.relpapel.msbookpayments.MsBookPayments.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Modelo que mapea de la base de datos usando JPA.
 */
@Entity
@Data
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libroId;
    private int cantidad;
    private double precioTotal;

}
