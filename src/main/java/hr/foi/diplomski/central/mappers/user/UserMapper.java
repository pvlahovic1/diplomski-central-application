package hr.foi.diplomski.central.mappers.user;

import hr.foi.diplomski.central.model.User;
import hr.foi.diplomski.central.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto entitysToDto(User user);

    List<UserDto> entitysToDtos(List<User> users);

}
