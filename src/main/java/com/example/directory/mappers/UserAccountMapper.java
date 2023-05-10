package com.example.directory.mappers;

import com.example.directory.dto.UserAccountDto;
import com.example.directory.model.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    @Mappings({
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password")
    })
    UserAccount userDtoToUser(UserAccountDto userAccountDto);
}
