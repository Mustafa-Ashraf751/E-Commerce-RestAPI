package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
  @Query(value = "SELECT u.user_id ,u.first_name , u.last_name,u.email,u.mobile_number,u.password " +
          "FROM users u " +
          "JOIN user_address ua ON ua.user_id = u.user_id " +
          "JOIN addresses a ON ua.address_id = a.address_id " +
          "WHERE a.address_id =?1", nativeQuery = true)
  List<User> findByAddress(Long id);

  @Query(value = "SELECT * FROM users u WHERE u.email =?1", nativeQuery = true)
  Optional<User> findUserByEmail(String userEmail);
}
