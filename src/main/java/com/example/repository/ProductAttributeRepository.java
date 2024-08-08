package com.example.repository;

import com.example.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Integer>
{
    Optional<ProductAttribute> findByName(String name);
}
