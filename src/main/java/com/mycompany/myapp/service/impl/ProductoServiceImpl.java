package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.enumeration.TipoIva;
import com.mycompany.myapp.repository.ProductoRepository;
import com.mycompany.myapp.repository.specification.ProductoSpecification;
import com.mycompany.myapp.service.ProductoService;
import com.mycompany.myapp.service.dto.ProductoDTO;
import com.mycompany.myapp.service.mapper.ProductoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDTO save(ProductoDTO productoDTO) {
        log.debug("Request to save Producto : {}", productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);

        if (null == producto.getPrecioTotal()) {
            producto.setPrecioTotal(0.0);

            if (null != producto.getTipoIva()) {
                if (producto.getTipoIva() == TipoIva.A) {
                    producto.setPrecioTotal(producto.getPrecioBase() * 1.04);
                }

                if (producto.getTipoIva() == TipoIva.B) {
                    producto.setPrecioTotal(producto.getPrecioBase() * 1.10);
                }

                if (producto.getTipoIva() == TipoIva.C) {
                    producto.setPrecioTotal(producto.getPrecioBase() * 1.21);
                }
            }
        }
        producto = productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }

    @Override
    public Optional<ProductoDTO> partialUpdate(ProductoDTO productoDTO) {
        log.debug("Request to partially update Producto : {}", productoDTO);

        return productoRepository
            .findById(productoDTO.getId())
            .map(existingProducto -> {
                productoMapper.partialUpdate(existingProducto, productoDTO);

                return existingProducto;
            })
            .map(productoRepository::save)
            .map(productoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Productos");
        return productoRepository.findAll(pageable).map(productoMapper::toDto);
    }

    @Override
    public Page<ProductoDTO> findAllBySearchingParam(String filter, Pageable pageable) {
        log.debug("Request to get simple search of producto, specification");
        return productoRepository.findAll(ProductoSpecification.searchingParam(filter), pageable).map(productoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id).map(productoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findAllByCodigo(String codigo, Pageable pageable) {
        log.debug("Request to get all Productos");
        return productoRepository.productoByCodigo(codigo, pageable).map(productoMapper::toDto);
    }
}
