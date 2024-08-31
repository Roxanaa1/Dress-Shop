package com.example.model.dtos;

import com.example.model.PaymentMethod;
import lombok.Data;

import java.util.List;
@Data
public class CartDTO
{
    private int id;
    private int userId;
    private float totalPrice;
    private List<CartEntryDTO> cartEntries;
    private PaymentMethod paymentMethod;
}
