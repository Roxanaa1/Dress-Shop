package com.example.mapper;

import com.example.model.Cart;
import com.example.model.CartEntry;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.CartEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartMapper
{
    @Mapping(source = "user.id", target = "userId")
    CartDTO cartToCartDTO(Cart cart);

    @Mapping(source = "userId", target = "user")
    Cart cartDTOToCart(CartDTO cartDTO);

    default User mapUserId(int userId)
    {
        User user = new User();
        user.setId(userId);
        return user;
    }

    default int mapUser(User user)
    {
        return user != null ? user.getId() : 0;
    }

    default CartEntryDTO mapCartEntry(CartEntry cartEntry)
    {
        CartEntryDTO cartEntryDTO = new CartEntryDTO();
        cartEntryDTO.setId(cartEntry.getId());
        cartEntryDTO.setCartId(cartEntry.getCart().getId());
        cartEntryDTO.setProductId(cartEntry.getProduct().getId());
        cartEntryDTO.setQuantity(cartEntry.getQuantity());
        cartEntryDTO.setPricePerPiece(cartEntry.getPricePerPiece());
        cartEntryDTO.setTotalPricePerEntry(cartEntry.getTotalPricePerEntry());
        return cartEntryDTO;
    }

    default CartEntry mapCartEntryDTO(CartEntryDTO cartEntryDTO)
    {
        CartEntry cartEntry = new CartEntry();
        cartEntry.setId(cartEntryDTO.getId());
        cartEntry.setQuantity(cartEntryDTO.getQuantity());
        cartEntry.setPricePerPiece(cartEntryDTO.getPricePerPiece());
        cartEntry.setTotalPricePerEntry(cartEntryDTO.getTotalPricePerEntry());

        Cart cart = new Cart();
        cart.setId(cartEntryDTO.getCartId());
        cartEntry.setCart(cart);

        Product product = new Product();
        product.setId(cartEntryDTO.getProductId());
        cartEntry.setProduct(product);

        return cartEntry;
    }

    default Cart mapCartId(int cartId)
    {
        Cart cart = new Cart();
        cart.setId(cartId);
        return cart;
    }

    default int mapCart(Cart cart)
    {
        return cart != null ? cart.getId() : 0;
    }
}
