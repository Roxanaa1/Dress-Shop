package com.example.model.dtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import java.time.LocalDate;
import java.util.List;

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
    private List<CartEntryDTO> cartEntries;
    private List<ProductProductAttributeDTO> productAttributeAttributeValues;
    private List<String> productImages;

}
