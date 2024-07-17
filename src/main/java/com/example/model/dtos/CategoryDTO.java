package com.example.model.dtos;

import lombok.*;

import java.util.List;
@Data
public class CategoryDTO
{
    private int id;
    private String name;
    private String description;
    private List<Integer> productIds;

}
