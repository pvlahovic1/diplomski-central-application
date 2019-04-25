package hr.foi.diplomski.central.service.roles;

import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;

import java.util.List;

public interface RolesService {

    List<RolesDto> findAllRoles();

}
