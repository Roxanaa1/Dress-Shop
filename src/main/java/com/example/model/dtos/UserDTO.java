package com.example.model.dtos;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
public class UserDTO
{
    private int id;
    private String firstName;
    private String lastName;
    @NotNull
    private String email;
    private String phoneNumber;
    private String password;
    private int defaultDeliveryAddress;
    private int defaultBillingAddress;
    private List<Integer> orderIds;
    private Integer cartId;
}
