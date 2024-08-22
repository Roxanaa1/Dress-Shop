package com.example.controller;

import com.example.model.dtos.WishlistDTO;
import com.example.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistController
{
    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(@PathVariable int userId, @RequestBody Map<String, Integer> productMap)
    {
        try {
            Integer productId = Optional.ofNullable(productMap.get("productId"))
                    .orElseThrow(() -> new IllegalArgumentException("Product ID is required"));

           WishlistDTO wishlist = wishlistService.addProductToWishlist(userId, productId);
           return ResponseEntity.ok(wishlist);
        } catch (IllegalArgumentException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/removeItem/{wishlistItemId}")
    public ResponseEntity<?> removeItemFromWishlist(@PathVariable int wishlistItemId)
    {
        try {
            wishlistService.removeItemFromWishlist(wishlistItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistDTO>> getWishlistByUser(@PathVariable int userId)
    {
        try {
            List<WishlistDTO> wishlist = wishlistService.getWishlistByUser(userId);

            return ResponseEntity.ok(wishlist);
        } catch (Exception e)
        {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
