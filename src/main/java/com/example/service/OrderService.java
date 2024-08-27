package com.example.service;

import com.example.mapper.OrderMapper;
import com.example.model.*;
import com.example.model.dtos.OrderDTO;
import com.example.repository.AddressRepository;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderService
{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository,UserRepository userRepository,CartRepository cartRepository,AddressRepository addressRepository,OrderMapper orderMapper)
    {
        this.orderRepository=orderRepository;
        this.userRepository=userRepository;
        this.cartRepository=cartRepository;
        this.orderMapper=orderMapper;

    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO)
    {
        logger.info("Attempting to create order with details: {}", orderDTO);
        try {

            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + orderDTO.getUserId()));

            Order order = orderMapper.orderDTOToOrder(orderDTO);
            order.setOrderDate(LocalDate.now());
            order.setTotalPrice(orderDTO.getTotalPrice());
            System.out.println("Payment method received: " + orderDTO.getPaymentMethod());

            order.setPaymentMethod(PaymentMethod.valueOf(orderDTO.getPaymentMethod()));
            if (orderDTO.getPaymentMethod() == null || PaymentMethod.valueOf(orderDTO.getPaymentMethod()) == null)
            {
                throw new IllegalArgumentException("Invalid or missing payment method.");
            }


            if (orderDTO.getDeliveryAddress() == 0)
            {
                orderDTO.setDeliveryAddress(user.getDefaultDeliveryAddress());
                System.out.println(orderDTO.getDeliveryAddress());
            }
            if (orderDTO.getInvoiceAddress() == 0)
            {
                orderDTO.setInvoiceAddress(user.getDefaultBillingAddress());
                System.out.println(orderDTO.getInvoiceAddress());
            }

            order.setDeliveryAddress(orderDTO.getDeliveryAddress());
            order.setInvoiceAddress(orderDTO.getInvoiceAddress());

            Order savedOrder = orderRepository.save(order);
            logger.info("Order saved with ID: {}", savedOrder.getId());

            clearCart(orderDTO.getCartId());
            logger.info("Cart cleared for Cart ID: {}", orderDTO.getCartId());

            return savedOrder;
        } catch (Exception e)
        {
            logger.error("Error creating order: {}", e.getMessage(), e);
            throw e;
        }
    }


    @Transactional
    public void clearCart(int cartId)
    {
        cartRepository.findById(cartId).ifPresent(cart ->
        {
            logger.info("Clearing cart with ID: {}", cartId);
            cart.setCartEntries(new ArrayList<>());
            cart.setTotalPrice(0);
            Cart updatedCart = cartRepository.save(cart);
            logger.info("Cart cleared: {}", updatedCart.getCartEntries().isEmpty());
        });
    }


    public Optional<Order> getOrderById(int id)
    {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Order orderDetails, int id)
    {
        return orderRepository.findById(id).map(order ->
        {
            order.setUser(orderDetails.getUser());
            order.setCart(orderDetails.getCart());
            order.setPaymentMethod(orderDetails.getPaymentMethod());
            order.setDeliveryAddress(orderDetails.getDeliveryAddress());
            order.setInvoiceAddress(orderDetails.getInvoiceAddress());
            order.setTotalPrice(orderDetails.getTotalPrice());
            order.setOrderDate(orderDetails.getOrderDate());

            return orderRepository.save(order);
        }).orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }
    public void deleteOrder(int id)
    {
        if(orderRepository.existsById(id))
        {
            orderRepository.deleteById(id);
        }
        else
        {
            throw new EntityNotFoundException("Order not found with id:"+id);
        }
    }

}
