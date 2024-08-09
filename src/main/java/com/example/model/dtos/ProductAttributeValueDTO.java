package com.example.model.dtos;

import com.example.model.AttributeValue;
import com.example.model.ProductAttribute;
import lombok.Data;

@Data
public class ProductAttributeValueDTO
{
    private int id;
    private ProductAttribute productAttribute;
    private AttributeValue attributeValue;
}
