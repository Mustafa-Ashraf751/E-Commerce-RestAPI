package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.Dto.ProductDTO;
import com.project.e_commerce.e_commerce_project.Dto.ProductResponse;
import com.project.e_commerce.e_commerce_project.entity.Product;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

  ProductDTO createProduct(Long categoryId, Product product);
  ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
  ProductResponse getProductsByCategoryId(Long categoryId,Integer pageNumber,Integer pageSize,
                                          String sortBy,String sortOrder);
  ProductResponse getProductByKeyWord(String keyWord,Integer pageNumber,
                                      Integer pageSize,String sortBy, String sortOrder);
  ProductDTO updateProductById(Long productId,Product product);
  ProductDTO updateProductImage(MultipartFile file,Long productId);
  String deleteProduct(Long productId);

}
