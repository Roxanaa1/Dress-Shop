package com.example.model.dtos;

import lombok.Data;

@Data
public class ProductProductAttributeDTO
{
    private int id;
    private ProductAttributeDTO productAttribute;
    private AttributeValueDTO attributeValue;
}
