package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

  private Long CartItemId;
  private Double discount = 0.00;
  private Double productPrice;
  private Integer quantity;
  private CartDTO cart;
  private ProductDTO product;

}
