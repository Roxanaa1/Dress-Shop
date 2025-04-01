package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email")
    Optional<Boolean> existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    User findUserById(int id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPasswordAndVerifiedAccountTrue(String email, String password);

    @Query("""
                SELECT EXTRACT(MONTH FROM u.createdAt) AS month, COUNT(u)
                FROM User u
                WHERE EXTRACT(YEAR FROM u.createdAt) = :year
                GROUP BY EXTRACT(MONTH FROM u.createdAt)
                ORDER BY month
            """)
    List<Object[]> countUsersByMonth(@Param("year") int year);

}
