package com.supermercado.myapp.service;

import com.supermercado.myapp.service.dto.CajaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.supermercado.myapp.domain.Caja}.
 */
public interface CajaService {
    /**
     * Save a caja.
     *
     * @param cajaDTO the entity to save.
     * @return the persisted entity.
     */
    CajaDTO save(CajaDTO cajaDTO);

    /**
     * Partially updates a caja.
     *
     * @param cajaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CajaDTO> partialUpdate(CajaDTO cajaDTO);

    /**
     * Get all the cajas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CajaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" caja.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CajaDTO> findOne(Long id);

    /**
     * Delete the "id" caja.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
