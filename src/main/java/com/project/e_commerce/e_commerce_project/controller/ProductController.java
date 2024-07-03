package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.Dto.ProductDTO;
import com.project.e_commerce.e_commerce_project.Dto.ProductResponse;
import com.project.e_commerce.e_commerce_project.config.AppConstants;
import com.project.e_commerce.e_commerce_project.entity.Product;
import com.project.e_commerce.e_commerce_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("/admin/categories/{categoryId}/product")
  public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @RequestBody Product product){
    ProductDTO productDTO = productService.createProduct(categoryId,product);
    return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
  }

  @GetMapping("/public/products")
  public ResponseEntity<ProductResponse> getProducts(
   @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
   @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
   @RequestParam(name="sortBy",defaultValue = "productName",required = false) String sortBy,
   @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder

  ){
    ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
    return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
  }

  @GetMapping("/public/categories/{categoryId}/products")
  public ResponseEntity<ProductResponse> getProductsByCategoryId(@PathVariable Long categoryId,
    @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
    @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
    @RequestParam(name="sortBy",defaultValue = "product_name",required = false) String sortBy,
    @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder){
    ProductResponse productResponse = productService.getProductsByCategoryId(categoryId,pageNumber,
            pageSize,sortBy, sortOrder);
    return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
  }


  @GetMapping("/public/products/keyword/{keyWord}")
  public ResponseEntity<ProductResponse> getProductByKeyWord(@PathVariable String keyWord,
  @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
  @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
  @RequestParam(name="sortBy",defaultValue = "product_name",required = false) String sortBy,
  @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder){
    ProductResponse productResponse = productService.getProductByKeyWord(keyWord,pageNumber,
            pageSize,sortBy, sortOrder);
    return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
  }

  @PutMapping("/admin/products/{productId}")
  public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long productId,@RequestBody Product product){
    ProductDTO productDTO = productService.updateProductById(productId,product);
    return new ResponseEntity<>(productDTO,HttpStatus.OK);
  }

  @PutMapping("/admin/products/{productId}/{image}")
  public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                       @RequestPart("image") MultipartFile image){
    ProductDTO productDTO = productService.updateProductImage(image,productId);
    return new ResponseEntity<>(productDTO,HttpStatus.OK);
  }

  @DeleteMapping("/admin/products/{productId}")
  public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
    String message = productService.deleteProduct(productId);
    return new ResponseEntity<>(message,HttpStatus.OK);
  }


}
