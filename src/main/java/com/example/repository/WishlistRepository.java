package com.example.repository;

import com.example.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer>
{
    List<Wishlist> findByUserId(int userId);

    Wishlist findByUserIdAndProductId(int userId, int productId);
}
