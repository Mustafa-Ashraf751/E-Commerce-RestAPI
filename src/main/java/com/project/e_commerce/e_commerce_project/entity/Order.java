package com.project.e_commerce.e_commerce_project.entity;

import com.project.e_commerce.e_commerce_project.util.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @Email
  @NotNull(message = "please enter your email")
  private String email;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus = OrderStatus.NEW;
  private Double totalAmount;
  private LocalDate localDate;


  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();
// CascadeType.MERGE,CascadeType.PERSIST
}
