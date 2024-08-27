package com.example.repository;
import com.example.model.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CartEntryRepository extends JpaRepository<CartEntry,Integer>
{
    @Modifying//operatie de actualizare
    @Transactional
    @Query("DELETE FROM CartEntry ce WHERE ce.cart.id = :cartId")
    void deleteByCartId(int cartId);
}
