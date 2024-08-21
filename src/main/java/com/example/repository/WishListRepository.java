package com.example.repository;

import com.example.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer>
{
    boolean existsByUserIdAndProductId(int userId, int productId);
    WishList save(WishList wishlist);

}
