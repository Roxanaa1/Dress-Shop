package com.example.repository;

import com.example.model.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer> {
    AttributeValue findByValue(String value);
}
