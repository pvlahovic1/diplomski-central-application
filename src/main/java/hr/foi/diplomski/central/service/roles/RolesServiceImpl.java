package hr.foi.diplomski.central.service.roles;

import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;
import hr.foi.diplomski.central.mappers.roles.RoleMapper;
import hr.foi.diplomski.central.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RolesDto> findAllRoles() {
        return roleMapper.entityToDto(roleRepository.findAll());
    }
}
