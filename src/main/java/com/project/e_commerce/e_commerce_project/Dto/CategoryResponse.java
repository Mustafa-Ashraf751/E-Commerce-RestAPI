package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

  private List<CategoryDTO> content;
  private Integer pageNumber;
  private Integer pageSize;
  private Integer totalElement;
  private Integer totalPages;
  private boolean lastPage;

}
