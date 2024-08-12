package com.example.model.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AttributeValueDTO
{
    private int id;
    private String value;
    private List<Integer> productAttributeValueIds;
}
