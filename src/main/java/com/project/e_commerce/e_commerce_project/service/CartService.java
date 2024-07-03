package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.Dto.CartDTO;

import java.util.List;

public interface CartService {
  String deleteProductFromCart(Long productId,Long cartId);
  List<CartDTO> getAllCarts();
  CartDTO addProductsToCarts(Long cartId,Long productId,Integer quantity);
  CartDTO getCart(String emailId,Long cartId);
  CartDTO updateProductQuantityInCart(Long cartId,Long productId,Integer quantity);
  void updateProductInCart(Long cartId, Long productId);
}
