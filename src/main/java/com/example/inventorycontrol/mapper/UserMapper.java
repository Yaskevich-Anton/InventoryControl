package com.example.inventorycontrol.mapper;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toUserDto(User user);
    User toUser(UserDto userDto);


}
