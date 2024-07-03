package com.project.e_commerce.e_commerce_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @NotBlank
  @Size(min = 6 , message = "The product description must contain at least 6 characters")
  private String description;

  private String image;

  @NotBlank
  @Size(min = 3 , message = "The product name must contain at least 3 characters")
  private String productName;

  private Double discount = 0.00;
  private Double price=0.00;
  private Integer quantity;
  private Double specialPrice;

  @OneToMany(mappedBy = "product",cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
  private List<CartItem> products = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "product",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
  private List<OrderItem> orderItems = new ArrayList<>();

}
