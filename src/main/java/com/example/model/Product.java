package com.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private float price;

    @Column(name = "availablequantity", nullable = false)
    private int availableQuantity;

    @Column(name = "addeddate")
    private LocalDate addedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<CartEntry> cartEntries;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductProductAttribute> productAttributeAttributeValues;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;
}
