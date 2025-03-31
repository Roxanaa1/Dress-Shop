package com.example.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class MonthlyTotalDTO {
    private int month;
    private double total;

    public MonthlyTotalDTO(int month, double total) {
        this.month = month;
        this.total = total;
    }
}
