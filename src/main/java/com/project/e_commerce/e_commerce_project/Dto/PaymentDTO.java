package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

  private Long paymentId;
  private String paymentMethod;
}
