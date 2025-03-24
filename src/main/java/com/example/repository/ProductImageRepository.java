package com.example.repository;

import com.example.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    @Modifying
    @Query("DELETE FROM ProductImage p WHERE p.product.id = :productId")
    void deleteByProductId(@Param("productId") int productId);

}
