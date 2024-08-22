package com.example.service;

import com.example.mapper.WishlistMapper;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.Wishlist;
import com.example.model.dtos.WishlistDTO;
import com.example.repository.ProductRepository;
import com.example.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService
{
    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, WishlistMapper wishlistMapper, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistMapper = wishlistMapper;
        this.productRepository = productRepository;
    }

    @Transactional
    public WishlistDTO addProductToWishlist(int userId, int productId)
    {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // verific daca produsul exista deja in wishlist
        Wishlist existingWishlistItem = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existingWishlistItem != null)
        {
            return wishlistMapper.wishlistToWishlistDTO(existingWishlistItem);
        }

        Wishlist wishlistItem = new Wishlist();
        wishlistItem.setUser(new User(userId));
        wishlistItem.setProduct(product);

        // salvez wishlist ul si ret DTO ul
        Wishlist savedWishlistItem = wishlistRepository.save(wishlistItem);
        return wishlistMapper.wishlistToWishlistDTO(savedWishlistItem);
    }

    @Transactional
    public void removeItemFromWishlist(int wishlistItemId)
    {
        wishlistRepository.deleteById(wishlistItemId);
    }

    public List<WishlistDTO> getWishlistByUser(int userId)
    {
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);

        return wishlistItems.stream()
                .map(wishlistMapper::wishlistToWishlistDTO)
                .collect(Collectors.toList());
    }
}
