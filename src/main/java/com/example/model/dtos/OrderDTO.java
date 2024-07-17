package com.example.model.dtos;
import lombok.Data;
import java.time.LocalDate;
@Data
public class OrderDTO
{
    private int id;
    private int userId;
    private int cartId;
    private String paymentType;
    private int deliveryAddress;
    private int invoiceAddress;
    private float totalPrice;
    private LocalDate orderDate;
}
