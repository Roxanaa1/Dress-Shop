package com.example.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
public class WishList
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}

