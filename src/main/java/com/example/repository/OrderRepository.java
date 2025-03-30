package com.example.repository;

import com.example.model.Order;
import com.example.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>
{
    Optional<Order> findByCartIdAndUserIdAndOrderDateIsNull(int cartId,int userId);
    List<Order> findByOrderStatusAndOrderDateBefore(OrderStatus status, LocalDate date);

}
