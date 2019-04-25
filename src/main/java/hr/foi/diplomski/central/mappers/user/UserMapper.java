package hr.foi.diplomski.central.mappers.user;

import hr.foi.diplomski.central.controllers.api.users.data.UserDto;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.mappers.roles.RoleMapper;
import hr.foi.diplomski.central.model.User;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoleMapper.class, EntityResolver.class})
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "password", ignore = true)
    public abstract UserDto entityTODto(User user);

    public abstract List<UserDto> entityToDto(List<User> users);

    @Mapping(target = "password", ignore = true)
    public abstract User dtoToEntity(UserDto dto);

    @AfterMapping
    public User afterDtoToEntity(UserDto dto, @MappingTarget User user) {
        if (StringUtils.isNotBlank(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return user;
    }

}
