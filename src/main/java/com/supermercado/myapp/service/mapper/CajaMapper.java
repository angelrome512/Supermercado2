package com.supermercado.myapp.service.mapper;

import com.supermercado.myapp.domain.Caja;
import com.supermercado.myapp.service.dto.CajaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caja} and its DTO {@link CajaDTO}.
 */
@Mapper(componentModel = "spring", uses = { VentaMapper.class, EmpleadoMapper.class })
public interface CajaMapper extends EntityMapper<CajaDTO, Caja> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "id")
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "id")
    CajaDTO toDto(Caja s);
}
