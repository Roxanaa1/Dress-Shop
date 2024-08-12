package com.example.mapper;

import com.example.model.*;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.CartEntryDTO;
import com.example.model.dtos.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface CartMapper
{
    @Mapping(source = "user.id", target = "userId")
    CartDTO cartToCartDTO(Cart cart);

    @Mapping(source = "userId", target = "user")
    Cart cartDTOToCart(CartDTO cartDTO);

    @Mapping(source = "cart.id", target = "cartId")
    CartEntryDTO cartEntryToCartEntryDTO(CartEntry cartEntry);

    @Mapping(source = "cartId", target = "cart")
    @Mapping(source = "product", target = "product")
    CartEntry cartEntryDTOToCartEntry(CartEntryDTO cartEntryDTO);

    @Mapping(source = "productImages", target = "productImages", qualifiedByName = "mapProductImagesToStrings")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "productImages", target = "productImages", qualifiedByName = "mapStringsToProductImages")
    Product productDTOToProduct(ProductDTO productDTO);

    default User mapUserId(int userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    default int mapUser(User user) {
        return user != null ? user.getId() : 0;
    }

    default Cart mapCartId(int cartId) {
        Cart cart = new Cart();
        cart.setId(cartId);
        return cart;
    }

    default int mapCart(Cart cart) {
        return cart != null ? cart.getId() : 0;
    }

    default Product mapProductId(int productId) {
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    default int mapProduct(Product product) {
        return product != null ? product.getId() : 0;
    }

    @Named("mapProductImagesToStrings")
    default List<String> mapProductImagesToStrings(List<ProductImage> productImages) {
        return productImages.stream()
                .map(ProductImage::getCode)
                .collect(Collectors.toList());
    }

    @Named("mapStringsToProductImages")
    default List<ProductImage> mapStringsToProductImages(List<String> codes, Product product) {
        return codes.stream()
                .map(code -> {
                    ProductImage image = new ProductImage();
                    image.setCode(code);
                    image.setProduct(product);
                    return image;
                })
                .collect(Collectors.toList());
    }
}