package com.example.mapper;

import com.example.model.Cart;
import com.example.model.CartEntry;
import com.example.model.User;
import com.example.model.dtos.CartDTO;
import com.example.model.dtos.CartEntryDTO;
import com.example.model.dtos.ProductDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T00:48:16+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public CartDTO cartToCartDTO(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDTO cartDTO = new CartDTO();

        cartDTO.setUserId( cartUserId( cart ) );
        cartDTO.setId( cart.getId() );
        cartDTO.setTotalPrice( cart.getTotalPrice() );
        cartDTO.setCartEntries( cartEntryListToCartEntryDTOList( cart.getCartEntries() ) );
        cartDTO.setPaymentMethod( cart.getPaymentMethod() );

        return cartDTO;
    }

    @Override
    public Cart cartDTOToCart(CartDTO cartDTO) {
        if ( cartDTO == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setUser( mapUserIdToUser( cartDTO.getUserId() ) );
        cart.setId( cartDTO.getId() );
        cart.setTotalPrice( cartDTO.getTotalPrice() );
        cart.setCartEntries( cartEntryDTOListToCartEntryList( cartDTO.getCartEntries() ) );
        cart.setPaymentMethod( cartDTO.getPaymentMethod() );

        return cart;
    }

    @Override
    public CartEntryDTO cartEntryToCartEntryDTO(CartEntry cartEntry) {
        if ( cartEntry == null ) {
            return null;
        }

        CartEntryDTO cartEntryDTO = new CartEntryDTO();

        cartEntryDTO.setCartId( cartEntryCartId( cartEntry ) );
        cartEntryDTO.setProduct( productMapper.productToProductDTO( cartEntry.getProduct() ) );
        cartEntryDTO.setId( cartEntry.getId() );
        cartEntryDTO.setQuantity( cartEntry.getQuantity() );
        cartEntryDTO.setPricePerPiece( cartEntry.getPricePerPiece() );
        cartEntryDTO.setTotalPricePerEntry( cartEntry.getTotalPricePerEntry() );

        return cartEntryDTO;
    }

    @Override
    public CartEntry cartEntryDTOToCartEntry(CartEntryDTO cartEntryDTO) {
        if ( cartEntryDTO == null ) {
            return null;
        }

        CartEntry cartEntry = new CartEntry();

        cartEntry.setCart( mapCartIdToCart( cartEntryDTO.getCartId() ) );
        Integer id = cartEntryDTOProductId( cartEntryDTO );
        if ( id != null ) {
            cartEntry.setProduct( mapProductIdToProduct( id.intValue() ) );
        }
        cartEntry.setId( cartEntryDTO.getId() );
        cartEntry.setQuantity( cartEntryDTO.getQuantity() );
        cartEntry.setPricePerPiece( cartEntryDTO.getPricePerPiece() );
        cartEntry.setTotalPricePerEntry( cartEntryDTO.getTotalPricePerEntry() );

        return cartEntry;
    }

    private int cartUserId(Cart cart) {
        if ( cart == null ) {
            return 0;
        }
        User user = cart.getUser();
        if ( user == null ) {
            return 0;
        }
        int id = user.getId();
        return id;
    }

    protected List<CartEntryDTO> cartEntryListToCartEntryDTOList(List<CartEntry> list) {
        if ( list == null ) {
            return null;
        }

        List<CartEntryDTO> list1 = new ArrayList<CartEntryDTO>( list.size() );
        for ( CartEntry cartEntry : list ) {
            list1.add( cartEntryToCartEntryDTO( cartEntry ) );
        }

        return list1;
    }

    protected List<CartEntry> cartEntryDTOListToCartEntryList(List<CartEntryDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<CartEntry> list1 = new ArrayList<CartEntry>( list.size() );
        for ( CartEntryDTO cartEntryDTO : list ) {
            list1.add( cartEntryDTOToCartEntry( cartEntryDTO ) );
        }

        return list1;
    }

    private int cartEntryCartId(CartEntry cartEntry) {
        if ( cartEntry == null ) {
            return 0;
        }
        Cart cart = cartEntry.getCart();
        if ( cart == null ) {
            return 0;
        }
        int id = cart.getId();
        return id;
    }

    private Integer cartEntryDTOProductId(CartEntryDTO cartEntryDTO) {
        if ( cartEntryDTO == null ) {
            return null;
        }
        ProductDTO product = cartEntryDTO.getProduct();
        if ( product == null ) {
            return null;
        }
        int id = product.getId();
        return id;
    }
}
