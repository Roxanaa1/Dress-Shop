package com.example.service;

import com.example.model.dtos.CustomerOrderCountDTO;
import com.example.model.dtos.MonthlyCountDTO;
import com.example.model.dtos.MonthlyTotalDTO;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderChartsService {
    @Autowired
    private OrderRepository orderRepository;

    public List<MonthlyCountDTO> getOrdersByMonth(int year) {
        List<Object[]> results = orderRepository.countOrdersByMonthFilteredByYear(year);
        List<MonthlyCountDTO> counts = new ArrayList<>();
        for (Object[] row : results) {
            counts.add(new MonthlyCountDTO((int) row[0], ((Long) row[1]).intValue()));
        }
        return counts;
    }

    public Map<String, Integer> getOrdersByCounty(int year) {
        List<Object[]> results = orderRepository.countOrdersByCountyFilteredByYear(year);
        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], ((Long) row[1]).intValue());
        }
        return map;
    }

    public List<MonthlyTotalDTO> getRevenueByMonth(int year) {
        List<Object[]> results = orderRepository.sumOrderValuesByMonthFilteredByYear(year);
        List<MonthlyTotalDTO> totals = new ArrayList<>();
        for (Object[] row : results) {
            totals.add(new MonthlyTotalDTO((int) row[0], ((Double) row[1])));
        }
        return totals;
    }

    public Map<String, Integer> getOrderStatusDistribution(int year) {
        List<Object[]> results = orderRepository.countByStatusFilteredByYear(year);
        Map<String, Integer> map = new HashMap<>();
        for (Object[] row : results) {
            map.put(row[0].toString(), ((Long) row[1]).intValue());
        }
        return map;
    }

    public List<CustomerOrderCountDTO> getTopCustomers(int limit, int year) {
        List<Object[]> results = orderRepository.findTopCustomersFilteredByYear(PageRequest.of(0, limit), year);
        List<CustomerOrderCountDTO> list = new ArrayList<>();
        for (Object[] row : results) {
            list.add(new CustomerOrderCountDTO((String) row[0], ((Long) row[1]).intValue()));
        }
        return list;
    }

}
