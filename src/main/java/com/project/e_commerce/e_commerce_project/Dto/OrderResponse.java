package com.project.e_commerce.e_commerce_project.Dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

  private List<OrderDTO> content;
  private Integer pageNumber;
  private Integer pageSize;
  private Integer totalElements;
  private Integer totalPages;
  private boolean lastPage;


}
