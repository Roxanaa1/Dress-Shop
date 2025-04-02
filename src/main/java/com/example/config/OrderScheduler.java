package com.example.config;

import com.example.model.Order;
import com.example.model.OrderStatus;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
public class OrderScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(cron = "0 0 17 * * *")
    public void completeOldOrders() {
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);
        List<Order> ordersToUpdate = orderRepository
                .findByOrderStatusAndOrderDateBefore(OrderStatus.PENDING, twoDaysAgo);

        ordersToUpdate.forEach(order -> order.setOrderStatus(OrderStatus.COMPLETED));
        orderRepository.saveAll(ordersToUpdate);
    }
}
