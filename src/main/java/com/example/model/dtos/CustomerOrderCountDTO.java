package com.example.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CustomerOrderCountDTO {
    private String name;
    private int orders;

    public CustomerOrderCountDTO(String name, int orders) {
        this.name = name;
        this.orders = orders;
    }

}
