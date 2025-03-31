package com.example.repository;

import com.example.model.Order;
import com.example.model.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>
{
    Optional<Order> findByCartIdAndUserIdAndOrderDateIsNull(int cartId,int userId);
    List<Order> findByOrderStatusAndOrderDateBefore(OrderStatus status, LocalDate date);
    @Query("SELECT EXTRACT(MONTH FROM o.orderDate) AS month, COUNT(o) AS count FROM Order o GROUP BY month")
    List<Object[]> countOrdersByMonth();

    @Query("SELECT a.county, COUNT(o) FROM Order o JOIN o.user u JOIN Address a ON a.id = u.defaultDeliveryAddress GROUP BY a.county")
    List<Object[]> countOrdersByCounty();

    @Query("SELECT EXTRACT(MONTH FROM o.orderDate) AS month, SUM(o.totalPrice) AS total FROM Order o GROUP BY month")
    List<Object[]> sumOrderValuesByMonth();

    @Query("SELECT o.orderStatus, COUNT(o) FROM Order o GROUP BY o.orderStatus")
    List<Object[]> countByStatus();

    @Query("SELECT CONCAT(u.firstName, ' ', u.lastName), COUNT(o) FROM Order o JOIN o.user u GROUP BY u.id, u.firstName, u.lastName ORDER BY COUNT(o) DESC")
    List<Object[]> findTopCustomers(Pageable pageable);

}
