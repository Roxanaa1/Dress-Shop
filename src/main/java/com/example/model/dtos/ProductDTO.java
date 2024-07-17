package com.example.model.dtos;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import java.time.LocalDate;
@Data
public class ProductDTO
{
    private int id;
    private String name;
    private String description;
    private float price;
    private int availableQuantity;
    private LocalDate addedDate;
    @NotNull
    private int categoryId;
}
