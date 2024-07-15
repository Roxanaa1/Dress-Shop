package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "productAttribute")
    private List<ProductAttributeValue> productAttributeValues;

    @OneToMany(mappedBy = "productAttribute", cascade = CascadeType.ALL)
    private List<ProductProductAttribute> productProductAttributes;
}
