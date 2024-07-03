package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

  private Long orderItemId;
  private Double discount;
  private Double orderProductPrice;
  private Integer quantity;
  private ProductDTO product;
}
