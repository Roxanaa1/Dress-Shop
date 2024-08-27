package com.example.mapper;

import com.example.model.*;
import com.example.model.dtos.*;
import org.mapstruct.*;
@Mapper(componentModel = "spring", uses = {UserMapper.class, CartMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "invoiceAddress", target = "invoiceAddress")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "orderDate", target = "orderDate")
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
    @Mapping(target = "cart", source = "cartId", qualifiedByName = "mapCartIdToCart")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "invoiceAddress", source = "invoiceAddress")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "orderDate", ignore = true)  // orderDate is set in the service
    Order orderDTOToOrder(OrderDTO orderDTO);

    default User mapUserIdToUser(Integer userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    default Cart mapCartIdToCart(Integer cartId) {
        if (cartId == null) return null;
        Cart cart = new Cart();
        cart.setId(cartId);
        return cart;
    }
}
