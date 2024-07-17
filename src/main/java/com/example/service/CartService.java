package com.example.service;

import com.example.model.Cart;
import com.example.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Optional<Cart> findCartById(int id) {
        return cartRepository.findById(id);
    }
}
