package com.example.service;

import com.example.mapper.CartMapper;
import com.example.mapper.ProductMapper;
import com.example.model.Cart;
import com.example.model.CartEntry;
import com.example.model.Product;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.CartEntryDTO;
import com.example.model.dtos.ProductDTO;
import com.example.repository.CartEntryRepository;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService
{

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartEntryRepository cartEntryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public CartService(CartRepository cartRepository, CartMapper cartMapper,CartEntryRepository cartEntryRepository,ProductRepository productRepository,ProductMapper productMapper)
    {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartEntryRepository=cartEntryRepository;
        this.productRepository=productRepository;
        this.productMapper=productMapper;
    }

    @Transactional
    public CartDTO addToCart(int cartId, CartEntryDTO cartEntryDTO) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartEntry cartEntry = cartMapper.cartEntryDTOToCartEntry(cartEntryDTO);

        Product product = productRepository.findById(cartEntryDTO.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Utilizează ProductMapper pentru a converti Product în ProductDTO dacă este necesar
        ProductDTO productDTO = productMapper.productToProductDTO(product);

        if (product.getAvailableQuantity() < cartEntryDTO.getQuantity()) {
            throw new RuntimeException("Insufficient quantity available");
        }

        cartEntry.setProduct(product);
        cartEntry.setCart(cart);

        float totalPricePerEntry = cartEntryDTO.getQuantity() * cartEntryDTO.getPricePerPiece();
        cartEntry.setTotalPricePerEntry(totalPricePerEntry);

        product.setAvailableQuantity(product.getAvailableQuantity() - cartEntryDTO.getQuantity());

        cart.getCartEntries().add(cartEntry);

        float updatedTotalPrice = cart.getTotalPrice() + totalPricePerEntry;
        cart.setTotalPrice(updatedTotalPrice);

        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.cartToCartDTO(updatedCart);
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
