package com.example.service;

import com.example.mapper.CartMapper;
import com.example.model.Cart;
import com.example.model.dtos.CartDTO;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService
{

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Autowired
    public CartService(CartRepository cartRepository, CartMapper cartMapper)
    {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public List<CartDTO> getAllCarts()
    {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(cartMapper::cartToCartDTO).collect(Collectors.toList());
    }

    public CartDTO getCartById(int id)
    {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.cartToCartDTO(cart);
    }

    public CartDTO createCart(CartDTO cartDTO)
    {
        Cart cart = cartMapper.cartDTOToCart(cartDTO);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.cartToCartDTO(savedCart);
    }

    public CartDTO updateCart(int id, CartDTO cartDTO)
    {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setTotalPrice(cartDTO.getTotalPrice());
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.cartToCartDTO(updatedCart);
    }

    public void deleteCart(int id)
    {
        cartRepository.deleteById(id);
    }
}
