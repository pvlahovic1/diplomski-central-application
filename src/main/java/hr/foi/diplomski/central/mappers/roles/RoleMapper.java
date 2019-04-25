package hr.foi.diplomski.central.mappers.roles;

import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;
import hr.foi.diplomski.central.model.Rola;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    @Mapping(source = "naziv", target = "itemName")
    RolesDto entityToDto(Rola entity);

    List<RolesDto> entityToDto(List<Rola> entity);

}
