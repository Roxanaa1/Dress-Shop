package com.example.repository;

import com.example.model.ProductProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductProductAttributeRepository extends JpaRepository<ProductProductAttribute,Integer>
{

}
