package com.mycompany.myapp.repository;

import java.util.List;

import com.mycompany.myapp.domain.Venta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // @Query("select max(numero_factura) from Venta v")
    // List<Venta> ultimaVenta();
}
