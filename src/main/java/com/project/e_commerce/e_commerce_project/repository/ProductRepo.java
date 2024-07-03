package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

  @Query(value = "SELECT * FROM products p " +
          "WHERE p.category_id =?1",nativeQuery = true)
  Page<Product> findByCategoryId(Long categoryId, Pageable pageDetails);

  @Query(value = "SELECT * FROM products p " +
          "WHERE p.product_name = ?1",nativeQuery = true)
  Page<Product> findByKeyWord(String keyWord,Pageable pageDetails);
//,
//  countQuery = "SELECT count(*) FROM products p WHERE p.category_id =?1"
}
