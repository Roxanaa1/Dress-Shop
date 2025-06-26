package com.example.mapper;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.PaymentMethod;
import com.example.model.User;
import com.example.model.dtos.OrderDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T00:48:17+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public OrderDTO orderToOrderDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setUserId( orderUserId( order ) );
        orderDTO.setCartId( orderCartId( order ) );
        if ( order.getPaymentMethod() != null ) {
            orderDTO.setPaymentMethod( order.getPaymentMethod().name() );
        }
        orderDTO.setDeliveryAddress( order.getDeliveryAddress() );
        orderDTO.setInvoiceAddress( order.getInvoiceAddress() );
        orderDTO.setTotalPrice( order.getTotalPrice() );
        orderDTO.setOrderDate( order.getOrderDate() );
        orderDTO.setId( order.getId() );

        return orderDTO;
    }

    @Override
    public Order orderDTOToOrder(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Order order = new Order();

        order.setUser( cartMapper.mapUserIdToUser( orderDTO.getUserId() ) );
        order.setCart( cartMapper.mapCartIdToCart( orderDTO.getCartId() ) );
        if ( orderDTO.getPaymentMethod() != null ) {
            order.setPaymentMethod( Enum.valueOf( PaymentMethod.class, orderDTO.getPaymentMethod() ) );
        }
        order.setDeliveryAddress( orderDTO.getDeliveryAddress() );
        order.setInvoiceAddress( orderDTO.getInvoiceAddress() );
        order.setTotalPrice( orderDTO.getTotalPrice() );
        order.setId( orderDTO.getId() );

        return order;
    }

    private int orderUserId(Order order) {
        if ( order == null ) {
            return 0;
        }
        User user = order.getUser();
        if ( user == null ) {
            return 0;
        }
        int id = user.getId();
        return id;
    }

    private int orderCartId(Order order) {
        if ( order == null ) {
            return 0;
        }
        Cart cart = order.getCart();
        if ( cart == null ) {
            return 0;
        }
        int id = cart.getId();
        return id;
    }
}
