package com.example.model.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserDTO
{
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int defaultDeliveryAddress;
    private int defaultBillingAddress;
}
