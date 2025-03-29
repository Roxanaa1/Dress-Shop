package com.example.model.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private float price;
    private int availableQuantity;
    private LocalDate addedDate;
    private CategoryDTO category;
    private float buyingPrice;
    //    private List<CartEntryDTO> cartEntries;
    private List<ProductProductAttributeDTO> productAttributeAttributeValues;
    private List<String> productImages;
    private List<AttributeWithValuesDTO> attributes;
}
