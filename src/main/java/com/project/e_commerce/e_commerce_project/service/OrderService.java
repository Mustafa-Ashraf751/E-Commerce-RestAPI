package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.Dto.OrderDTO;
import com.project.e_commerce.e_commerce_project.Dto.OrderResponse;

import java.util.List;

public interface OrderService {

  OrderDTO createOrder(String userEmail, Long cartId, String paymentMethod);
  OrderResponse getOrders(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
  List<OrderDTO> getOrderByEmailId(String email);
  OrderDTO getOrderByEmailIdAndOrderId(String email,Long orderId);
  OrderDTO updateOrderStatus(String email,Long orderId,String status);
}
