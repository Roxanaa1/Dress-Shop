package com.example.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Data
public class OrderDetailsDTO {
    private int orderId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private LocalDate orderDate;
    private String paymentMethod;
    private float totalPrice;
    private String orderStatus;

    private List<ProductInfoDTO> products;
}
