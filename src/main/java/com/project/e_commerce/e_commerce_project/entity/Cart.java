package com.project.e_commerce.e_commerce_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cartId;

  private Double totalPrice = 0.00;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "cart",cascade = {CascadeType.MERGE,CascadeType.PERSIST},orphanRemoval = true)
  private List<CartItem> cartItems = new ArrayList<>();
}
