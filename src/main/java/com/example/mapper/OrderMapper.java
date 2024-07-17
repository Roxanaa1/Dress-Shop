package com.example.mapper;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.User;
import com.example.model.dtos.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cart.id", target = "cartId")
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "cartId", target = "cart")
    Order orderDTOToOrder(OrderDTO orderDTO);

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
}
