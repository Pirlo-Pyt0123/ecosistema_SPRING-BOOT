package com.g5.relpapel.msbookpayments.MsBookPayments.controller;

import com.g5.relpapel.msbookpayments.MsBookPayments.model.Compra;
import com.g5.relpapel.msbookpayments.MsBookPayments.model.Item;
import com.g5.relpapel.msbookpayments.MsBookPayments.repository.CompraRepository;
import com.g5.relpapel.msbookpayments.MsBookPayments.service.BuscadorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.g5.relpapel.msbookpayments.MsBookPayments.MsBookPaymentsApplication.API_NAME;

@RestController
@RequestMapping(API_NAME + "/compras")
@AllArgsConstructor

public class CompraController {

    private final BuscadorService buscadorService;

    private final CompraRepository compraRepository;

    /**
     * usamos la notación @Transactional para que se registre la venta sólo cuando se hubiera podido notificar al otro microservicio sobre el registro de la compra
     *
     * @param compra Datos de la compra que se desea registrar
     * @return Mensaje sobre resultado de la operación
     */
    @PostMapping
    @Transactional
    public ResponseEntity<String> registrarCompra(@RequestBody Compra compra) throws Exception {
        Item book = buscadorService.validarItem(compra.getLibroId(), compra.getCantidad());
        if (book == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item no válido, no visible o fuera de stock");
        } else {
            compraRepository.save(compra);
            buscadorService.notificarCompra(book, compra.getCantidad());
            return ResponseEntity.status(HttpStatus.CREATED).body("Compra registrada con éxito");
        }
    }
}

