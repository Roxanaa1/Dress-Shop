package com.example.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyUserCountDTO {
    private int month;
    private long count;

    public MonthlyUserCountDTO(int month, long count) {
        this.month = month;
        this.count = count;
    }
}
