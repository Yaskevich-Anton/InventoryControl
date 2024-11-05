package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE phone_number = :phone")
    Optional<User> findByPhone(@Param("phone") String phone);
}
