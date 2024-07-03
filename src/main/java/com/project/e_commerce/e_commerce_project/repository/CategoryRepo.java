package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepo extends JpaRepository<Category,Long> {

@Query(value = "SELECT * FROM categories c WHERE c.category_name = ?1",nativeQuery = true)
  Category findByCategoryName(String categoryName );
}
//"SELECT c FROM Category c WHERE c.categoryName = :category"