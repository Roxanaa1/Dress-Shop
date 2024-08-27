package com.example.mapper;

import com.example.model.Address;
import com.example.model.dtos.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {


    @Mapping(target = "streetLine", source = "streetLine")
    @Mapping(target = "postalCode", source = "postalCode")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "county", source = "county")
    @Mapping(target = "country", source = "country")
    AddressDTO addressToAddressDTO(Address address);

    @Mapping(target = "streetLine", source = "streetLine")
    @Mapping(target = "postalCode", source = "postalCode")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "county", source = "county")
    @Mapping(target = "country", source = "country")
    Address addressDTOToAddress(AddressDTO addressDTO);
}
