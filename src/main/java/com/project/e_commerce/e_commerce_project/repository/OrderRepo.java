package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

  @Query(value = "SELECT * FROM orders o WHERE o.email=?1 AND o.order_id=?2",nativeQuery = true)
  Order findOrderByEmailAndOrderId(String email, Long orderId);

  @Query(value = "SELECT * FROM orders o WHERE o.email=?1",nativeQuery = true )
  List<Order> findAllByEmailAddress(String email);
}
