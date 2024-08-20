package com.example.model.dtos;

import lombok.Data;

@Data
public class AddressDTO
{
    private String streetLine;
    private String postalCode;
    private String city;
    private String county;
    private String country;
}
