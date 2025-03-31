package com.example.model.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MonthlyCountDTO {
    private int month;
    private int count;

    public MonthlyCountDTO(int month, int count) {
        this.month = month;
        this.count = count;
    }
}
