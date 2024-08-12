package com.example.repository;

import com.example.model.Cart;
import com.example.model.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEntryRepository extends JpaRepository<CartEntry,Integer>
{

}
