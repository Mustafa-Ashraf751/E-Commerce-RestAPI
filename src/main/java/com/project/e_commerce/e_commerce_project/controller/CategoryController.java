package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.Dto.CategoryDTO;
import com.project.e_commerce.e_commerce_project.Dto.CategoryResponse;
import com.project.e_commerce.e_commerce_project.config.AppConstants;
import com.project.e_commerce.e_commerce_project.entity.Category;
import com.project.e_commerce.e_commerce_project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

  private CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/public/categories")
  public ResponseEntity<CategoryResponse> getCategories(
  @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
  @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
  @RequestParam(name = "sortBy",defaultValue = "categoryName",required = false) String sortBy,
  @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder
  ){
    CategoryResponse categoryResponse = categoryService.getCategories(pageNumber,pageSize,sortBy,sortOrder);
    return new ResponseEntity<>(categoryResponse, HttpStatus.FOUND);
  }

  @PostMapping("admin/category")
  public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category){
    CategoryDTO categoryDTO = categoryService.createCategory(category);
    return new ResponseEntity<>(categoryDTO,HttpStatus.CREATED);
  }

  @PutMapping("/admin/categories/{categoryId}")
  public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId ,
                                                    @RequestBody Category category ){
    CategoryDTO categoryDTO = categoryService.updateCategory(category,categoryId);
    return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
  }

  @DeleteMapping("/admin/categories/{categoryId}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
    String message = categoryService.deleteCategory(categoryId);
    return new ResponseEntity<>(message,HttpStatus.OK);
  }

}
