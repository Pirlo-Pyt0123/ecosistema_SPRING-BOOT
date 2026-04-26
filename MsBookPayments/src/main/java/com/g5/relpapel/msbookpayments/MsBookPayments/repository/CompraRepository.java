package com.g5.relpapel.msbookpayments.MsBookPayments.repository;

import com.g5.relpapel.msbookpayments.MsBookPayments.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}

