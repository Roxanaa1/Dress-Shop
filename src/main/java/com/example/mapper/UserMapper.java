package com.example.mapper;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.User;
import com.example.model.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper
{


    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "orders", target = "orderIds", qualifiedByName = "ordersToOrderIds")
    @Mapping(source = "cart.id", target = "cartId")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "password", target = "password")
    User userDTOToUser(UserDTO userDTO);

    @Named("ordersToOrderIds")
    default List<Integer> ordersToOrderIds(List<Order> orders)
    {
        return orders.stream().map(Order::getId).collect(Collectors.toList());
    }

    @Named("cartToCartId")
    default Integer cartToCartId(Cart cart)
    {
        return cart != null ? cart.getId() : null;
    }
}
