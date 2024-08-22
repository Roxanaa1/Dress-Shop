package com.example.mapper;

import com.example.model.Product;
import com.example.model.User;
import com.example.model.Wishlist;
import com.example.model.dtos.ProductDTO;
import com.example.model.dtos.WishlistDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface WishlistMapper
{

    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product", target = "productDTO")
    WishlistDTO wishlistToWishlistDTO(Wishlist wishlist);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    @Mapping(source = "productDTO", target = "product")
    Wishlist wishlistDTOToWishlist(WishlistDTO wishlistDTO);

    @Named("mapUserIdToUser")
    default User mapUserIdToUser(int userId)
    {
        User user = new User();
        user.setId(userId);
        return user;
    }
}
