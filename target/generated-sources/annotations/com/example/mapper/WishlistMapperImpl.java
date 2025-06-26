package com.example.mapper;

import com.example.model.User;
import com.example.model.Wishlist;
import com.example.model.dtos.WishlistDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T00:48:17+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public WishlistDTO wishlistToWishlistDTO(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        WishlistDTO wishlistDTO = new WishlistDTO();

        wishlistDTO.setUserId( wishlistUserId( wishlist ) );
        wishlistDTO.setProductDTO( productMapper.productToProductDTO( wishlist.getProduct() ) );
        wishlistDTO.setId( wishlist.getId() );

        return wishlistDTO;
    }

    @Override
    public Wishlist wishlistDTOToWishlist(WishlistDTO wishlistDTO) {
        if ( wishlistDTO == null ) {
            return null;
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser( mapUserIdToUser( wishlistDTO.getUserId() ) );
        wishlist.setProduct( productMapper.productDTOToProductManual( wishlistDTO.getProductDTO() ) );
        wishlist.setId( wishlistDTO.getId() );

        return wishlist;
    }

    private int wishlistUserId(Wishlist wishlist) {
        if ( wishlist == null ) {
            return 0;
        }
        User user = wishlist.getUser();
        if ( user == null ) {
            return 0;
        }
        int id = user.getId();
        return id;
    }
}
