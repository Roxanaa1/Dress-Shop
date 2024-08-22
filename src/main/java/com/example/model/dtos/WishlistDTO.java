package com.example.model.dtos;

import lombok.Data;

@Data
public class WishlistDTO
{
    private int id;

    private int userId;

    private ProductDTO productDTO;
}
