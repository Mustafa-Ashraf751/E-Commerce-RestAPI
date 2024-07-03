package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.Dto.CategoryDTO;
import com.project.e_commerce.e_commerce_project.Dto.CategoryResponse;
import com.project.e_commerce.e_commerce_project.entity.Category;

public interface CategoryService {
  CategoryDTO createCategory(Category category);
  CategoryDTO updateCategory(Category category,Long categoryId);
  CategoryResponse getCategories(Integer pageNumber , Integer pageSize, String sortBy, String sortOrder);
  String deleteCategory(Long categoryId);

}
