package com.example.repository;

import com.example.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
}
