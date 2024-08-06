package com.example.model.dtos;

import lombok.*;

import java.util.List;

@Data
public class UserDTO
{
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private int defaultDeliveryAddress;
    private int defaultBillingAddress;
    private List<Integer> orderIds;
    private List<Integer> cartIds;
}
