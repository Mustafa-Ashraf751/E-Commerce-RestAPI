package com.project.e_commerce.e_commerce_project.Dto;

import com.project.e_commerce.e_commerce_project.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

  private Long orderId;
  private String email;
  private OrderStatus orderStatus;
  private Double totalAmount;
  private LocalDate localDate;
  private PaymentDTO payment;
  private List<OrderItemDTO> orderItems = new ArrayList<>();

}
