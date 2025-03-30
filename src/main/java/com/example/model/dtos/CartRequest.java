package com.example.model.dtos;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class CartRequest {
    private List<String> productNames;
    private List<Long> prices;
    private List<Long> quantities;
}

