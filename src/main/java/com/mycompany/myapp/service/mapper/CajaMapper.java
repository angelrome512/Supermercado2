package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Caja;
import com.mycompany.myapp.service.dto.CajaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caja} and its DTO {@link CajaDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmpleadoMapper.class })
public interface CajaMapper extends EntityMapper<CajaDTO, Caja> {
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "id")
    CajaDTO toDto(Caja s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CajaDTO toDtoId(Caja caja);
}
