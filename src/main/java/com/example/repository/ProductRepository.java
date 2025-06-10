package com.example.repository;

import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    List<Product> findByNameContainingIgnoreCase(String query);

    @Query(value = "SELECT EXTRACT(MONTH FROM p.addeddate) AS month, COUNT(*) AS count " +
            "FROM product p GROUP BY month ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> countProductsByMonth();

    @Query(value = "SELECT EXTRACT(MONTH FROM p.addeddate) AS month, COUNT(*) AS count " +
            "FROM product p WHERE EXTRACT(YEAR FROM p.addeddate) = :year " +
            "GROUP BY month ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> countProductsByMonth(@Param("year") int year);

    @Query(value = "SELECT c.name AS category, COUNT(p.id) AS count " +
            "FROM product p JOIN category c ON p.category_id = c.id " +
            "GROUP BY c.name", nativeQuery = true)
    List<Object[]> countProductsByCategory();

    @Query(value = "SELECT p.name AS productName, SUM(ce.quantity) AS unitsSold " +
            "FROM cart_entry ce " +
            "JOIN product p ON ce.product_id = p.id " +
            "WHERE ce.order_id IS NOT NULL " +
            "GROUP BY p.name ORDER BY unitsSold DESC LIMIT 5", nativeQuery = true)
    List<Map<String, Object>> findTop5SoldProducts();

    @Query(value = "SELECT EXTRACT(MONTH FROM o.order_date) AS month, SUM(ce.quantity) AS sales " +
            "FROM cart_entry ce " +
            "JOIN orders o ON ce.order_id = o.id " +
            "WHERE ce.product_id = :productId " +
            "GROUP BY month ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getMonthlySalesForProduct(@Param("productId") int productId);

    @Query(value = "SELECT EXTRACT(MONTH FROM o.order_date) AS month, SUM(ce.quantity) AS sales " +
            "FROM cart_entry ce " +
            "JOIN orders o ON ce.order_id = o.id " +
            "WHERE ce.product_id = :productId AND EXTRACT(YEAR FROM o.order_date) = :year " +
            "GROUP BY month ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> getMonthlySalesForProduct(@Param("productId") int productId, @Param("year") int year);

    @Query(value = "SELECT p.name AS productName, SUM(ce.quantity) AS unitsSold, p.price " +
            "FROM cart_entry ce " +
            "JOIN product p ON ce.product_id = p.id " +
            "WHERE ce.order_id IS NOT NULL " +
            "GROUP BY p.name, p.price " +
            "ORDER BY unitsSold DESC LIMIT 1", nativeQuery = true)
    Map<String, Object> findMostSoldProduct();

    @Query(value = "SELECT p.name AS productName, SUM(ce.quantity * (p.price - p.buying_price)) AS totalProfit " +
            "FROM cart_entry ce " +
            "JOIN product p ON ce.product_id = p.id " +
            "WHERE ce.order_id IS NOT NULL " +
            "GROUP BY p.name ORDER BY totalProfit DESC LIMIT 1", nativeQuery = true)
    Map<String, Object> findMostProfitableProduct();


    @Query("SELECT p FROM Product p " +
            "JOIN p.category c " +
            "JOIN p.productAttributeAttributeValues pav " +
            "JOIN pav.attributeValue av " +
            "WHERE (:category IS NULL OR c.name = :category) " +
            "AND (:color IS NULL OR av.value = :color) " +
            "AND (:size IS NULL OR av.value = :size)")
    List<Product> findByCategoryAndAttributes(@Param("category") String category,
                                              @Param("color") String color,
                                              @Param("size") String size);
}
