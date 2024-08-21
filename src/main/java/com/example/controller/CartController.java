package com.example.controller;

import com.example.model.dtos.AddressDTO;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.CartEntryDTO;
import com.example.service.CartService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController
{


    private final CartService cartService;
    private final UserService userService;
    @Autowired
    public CartController(CartService cartService,UserService userService)
    {
        this.cartService=cartService;
        this.userService=userService;
    }

    @GetMapping("/getAllCarts")
    public ResponseEntity<List<CartDTO>> getAllCarts()
    {
        List<CartDTO> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/getCartById/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable int id)
    {
        CartDTO cart = cartService.getCartById(id);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("createCart")
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO)
    {
        CartDTO newCart = cartService.createCart(cartDTO);
        return ResponseEntity.ok(newCart);
    }

    @PostMapping("/addToCart/{cartId}")
    public ResponseEntity<CartDTO> addToCart(@PathVariable int cartId, @RequestBody CartEntryDTO cartEntryDTO)
    {
        if (cartEntryDTO == null || cartEntryDTO.getProduct() == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        System.out.println("Cart ID received in backend: " + cartId);

        CartDTO updatedCart = cartService.addToCart(cartId, cartEntryDTO);
        return ResponseEntity.ok(updatedCart);
    }



    @DeleteMapping("/removeItem/{itemId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable int itemId)
    {
        try {
            cartService.removeItem(itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PutMapping("/updateCart/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable int id, @RequestBody CartDTO cartDTO)
    {
        CartDTO updatedCart = cartService.updateCart(id, cartDTO);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/deleteCart/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable int id)
    {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }


}
