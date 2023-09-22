package com.example.directory.mappers;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastname", target = "lastname"),
            @Mapping(source = "country", target = "country"),
            @Mapping(source = "contactType", target = "contactType"),
            @Mapping(source = "value", target = "value"),
    })
    Contact contactDtoToContact(ContactDto contactDto);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastname", target = "lastname"),
            @Mapping(source = "country", target = "country"),
            @Mapping(source = "contactType", target = "contactType"),
            @Mapping(source = "value", target = "value"),
    })
    ContactDto contactToContactDto(Contact contact);
}
