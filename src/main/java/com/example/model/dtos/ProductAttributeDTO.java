package com.example.model.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProductAttributeDTO {
    private int id;
    private String name;
    private List<AttributeValueDTO> values;
}
