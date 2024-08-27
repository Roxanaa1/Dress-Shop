package com.example.controller;

import com.example.mapper.OrderMapper;
import com.example.model.Order;
import com.example.model.dtos.OrderDTO;
import com.example.service.CartService;
import com.example.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller

@RequestMapping("/orders")
public class OrderController
{
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    public OrderController(OrderService orderService,OrderMapper orderMapper,CartService cartService)
    {
        this.orderService=orderService;
        this.orderMapper=orderMapper;
        this.cartService=cartService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO)
    {
        try {
            Order order = orderService.createOrder(orderDTO);
            OrderDTO createdOrderDTO = orderMapper.orderToOrderDTO(order);
            return new ResponseEntity<>(createdOrderDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create order: {}", e.getMessage(), e);
            throw e;
        }
    }



    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id)
    {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(o -> ResponseEntity.ok(orderMapper.orderToOrderDTO(o)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateOrder/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable int id, @RequestBody OrderDTO orderDTO)
    {
        try {
            Order orderDetails = orderMapper.orderDTOToOrder(orderDTO);
            Order updatedOrder = orderService.updateOrder(orderDetails, id);
            OrderDTO updatedOrderDTO = orderMapper.orderToOrderDTO(updatedOrder);
            return ResponseEntity.ok(updatedOrderDTO);
        } catch (EntityNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id)
    {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

}
