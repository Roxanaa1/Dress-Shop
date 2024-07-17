package com.example.mapper;

import com.example.model.User;
import com.example.model.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")//trebuie sa fie gestionata de Spring ca un bean
public interface UserMapper
{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);

    @Mapping(source = "password", target = "password")
    User userDTOToUser(UserDTO userDTO);
}
