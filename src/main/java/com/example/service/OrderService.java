package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService
{

    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository)
    {
        this.orderRepository=orderRepository;
    }

    public Order createOrder(Order order)
    {
        return orderRepository.save(order);
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
            order.setPaymentType(orderDetails.getPaymentType());
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
