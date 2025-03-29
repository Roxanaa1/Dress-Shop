package com.example.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductInfoDTO {
    private String name;
    private float price;
    private String category;
    private String description;
    private String imageUrl;
}
