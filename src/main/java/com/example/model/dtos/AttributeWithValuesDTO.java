package com.example.model.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AttributeWithValuesDTO {
    private String attributeName;
    private List<String> values;
}

