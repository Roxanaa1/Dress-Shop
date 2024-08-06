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

@Mapper(componentModel = "spring")//trebuie sa fie gestionata de Spring ca un bean
public interface UserMapper
{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "orders", target = "orderIds", qualifiedByName = "ordersToOrderIds")
    @Mapping(source = "carts", target = "cartIds", qualifiedByName = "cartsToCartIds")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "password", target = "password")
    User userDTOToUser(UserDTO userDTO);

    @Named("ordersToOrderIds")
    default List<Integer> ordersToOrderIds(List<Order> orders)
    {
        return orders.stream().map(Order::getId).collect(Collectors.toList());
    }

    @Named("cartsToCartIds")
    default List<Integer> cartsToCartIds(List<Cart> carts)
    {
        return carts.stream().map(Cart::getId).collect(Collectors.toList());
    }
}
