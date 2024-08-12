package com.example.model.dtos;

import com.example.model.AttributeValue;
import com.example.model.ProductAttribute;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ProductAttributeValueDTO
{
    private int id;
    private ProductAttribute productAttribute;
    private AttributeValue attributeValue;
}
