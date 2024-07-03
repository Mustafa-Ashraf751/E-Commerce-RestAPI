package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

  private Long productId;
  private String productName;
  private String description;
  private String image;
  private Double discount = 0.00;
  private Double price = 0.00;
  private Integer quantity;
  private Double specialPrice;

}
