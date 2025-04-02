package com.example.mapper;

import com.example.model.Address;
import com.example.model.dtos.AddressDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-02T00:22:20+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDTO addressToAddressDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setStreetLine( address.getStreetLine() );
        addressDTO.setPostalCode( address.getPostalCode() );
        addressDTO.setCity( address.getCity() );
        addressDTO.setCounty( address.getCounty() );
        addressDTO.setCountry( address.getCountry() );

        return addressDTO;
    }

    @Override
    public Address addressDTOToAddress(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Address address = new Address();

        address.setStreetLine( addressDTO.getStreetLine() );
        address.setPostalCode( addressDTO.getPostalCode() );
        address.setCity( addressDTO.getCity() );
        address.setCounty( addressDTO.getCounty() );
        address.setCountry( addressDTO.getCountry() );

        return address;
    }
}
