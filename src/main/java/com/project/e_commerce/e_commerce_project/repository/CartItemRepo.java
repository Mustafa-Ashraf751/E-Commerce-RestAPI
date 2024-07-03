package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {

  @Query(value = "SELECT DISTINCT ci.* FROM carts_items ci "
  +"JOIN carts c ON c.cart_id = ci.cart_id "
  +"JOIN products p ON p.product_id = ci.product_id " +
          "Where c.cart_id = ?1 AND p.product_id = ?2",nativeQuery = true)
  CartItem findCartItemByProductIdAndCartId(Long cartId,Long productId);


  @Modifying
  @Query(value = "DELETE ci FROM carts_items ci "
  +"WHERE ci.cart_id = ?1 AND ci.product_id = ?2",nativeQuery = true)
  void deleteCartItemByProductIdAndCartId(Long cartId,Long productId);
}
