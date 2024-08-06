package com.example.model.dtos;

import lombok.Data;

@Data
public class CartEntryDTO
{
    private int id;
    private int cartId;
    private int productId;
    private int quantity;
    private float pricePerPiece;
    private float totalPricePerEntry;
}
