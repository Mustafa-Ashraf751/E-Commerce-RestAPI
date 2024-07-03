package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

  @Query(value = "SELECT DISTINCT c.* FROM carts c " +
          "JOIN users u ON c.user_id = u.user_id " +
          "WHERE c.cart_id=?1 AND u.email=?2",nativeQuery = true)
  Cart findCartByIdAndEmailId(Long cartId, String emailId);

  @Query(value = "SELECT DISTINCT c.* FROM carts c " +
          "JOIN carts_items ci ON c.cart_id = ci.cart_id " +
          "WHERE ci.product_id = ?1",nativeQuery = true)
  List<Cart> findCartsByProductId(Long productId);
}
