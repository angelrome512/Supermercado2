package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
<<<<<<< HEAD
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {}
=======
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.codigo = :codigo")
    Page<Producto> productoByCodigo(@Param("codigo") String codigo, Pageable pageable);
}
>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
