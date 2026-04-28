package com.g5.relpapel.msbookpayments.MsBookPayments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Modelo del Item a vender, este no mapea de la base de datos.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
public class Item {
    private String id;
    private String titulo;
    private String isbn;
    private int stock;
    private boolean visible;

    /**
     * Método para validar si está con stock y existe en stock la cantidad solicitada
     * @return true/false
     */
    public boolean isStockDisponible(int cantidadSolicitada) {
        return visible && stock >= cantidadSolicitada;
    }
}
