package com.example.mapper;

import com.example.model.*;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.CartEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = { ProductMapper.class })
public interface CartMapper
{
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "user.id", target = "userId")
    CartDTO cartToCartDTO(Cart cart);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    Cart cartDTOToCart(CartDTO cartDTO);

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product", target = "product")
    CartEntryDTO cartEntryToCartEntryDTO(CartEntry cartEntry);

    @Mapping(source = "cartId", target = "cart", qualifiedByName = "mapCartIdToCart")
    @Mapping(source = "product.id", target = "product", qualifiedByName = "mapProductIdToProduct")
    CartEntry cartEntryDTOToCartEntry(CartEntryDTO cartEntryDTO);

    @Named("mapUserIdToUser")
    default User mapUserIdToUser(int userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("mapCartIdToCart")
    default Cart mapCartIdToCart(int cartId) {
        Cart cart = new Cart();
        cart.setId(cartId);
        return cart;
    }

    @Named("mapProductIdToProduct")
    default Product mapProductIdToProduct(int productId) {
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}