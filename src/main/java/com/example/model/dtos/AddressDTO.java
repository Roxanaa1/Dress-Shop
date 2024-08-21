package com.example.model.dtos;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;


@Data
public class AddressDTO
{
    private String streetLine;
    private String postalCode;
    private String city;
    @NotNull
    private String county;
    private String country;
}
