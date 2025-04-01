package com.example.service;

import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ProductChartsService {
    @Autowired
    private  ProductRepository productRepository;

    public List<Map<String, Object>> getProductsAddedByMonth() {
        return productRepository.countProductsByMonth();
    }

    public List<Map<String, Object>> getProductsAddedByMonth(int year) {
        return productRepository.countProductsByMonth(year);
    }

    public Map<String, Long> getProductCountByCategory() {
        List<Object[]> results = productRepository.countProductsByCategory();
        Map<String, Long> categoryCount = new HashMap<>();
        for (Object[] row : results) {
            categoryCount.put((String) row[0], (Long) row[1]);
        }
        return categoryCount;
    }

    public List<Map<String, Object>> getTop5SoldProducts() {
        return productRepository.findTop5SoldProducts();
    }

    public List<Map<String, Object>> getMonthlySalesForProduct(int productId) {
        return productRepository.getMonthlySalesForProduct(productId);
    }

    public List<Map<String, Object>> getMonthlySalesForProduct(int productId, int year) {
        return productRepository.getMonthlySalesForProduct(productId, year);
    }

    public Map<String, Object> getMostSoldProduct() {
        return productRepository.findMostSoldProduct();
    }

    public Map<String, Object> getMostProfitableProduct() {
        return productRepository.findMostProfitableProduct();
    }
}
