package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.Dto.CartDTO;
import com.project.e_commerce.e_commerce_project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

  private CartService cartService;

  @Autowired
  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping("/admin/carts")
  public ResponseEntity<List<CartDTO>> getCarts(){
    List<CartDTO> cartDTOS = cartService.getAllCarts();
    return new ResponseEntity<>(cartDTOS, HttpStatus.FOUND);
  }

  @GetMapping("/public/users/{emailId}/carts/{cartId}")
  public ResponseEntity<CartDTO> getCart(@PathVariable String emailId,@PathVariable Long cartId){
    CartDTO cartDTO = cartService.getCart(emailId,cartId);
    return new ResponseEntity<>(cartDTO,HttpStatus.FOUND);
  }

  @PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
  public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long cartId,@PathVariable Long productId,
                                                  @PathVariable Integer quantity){
    CartDTO cartDTO = cartService.addProductsToCarts(cartId,productId,quantity);
    return new ResponseEntity<>(cartDTO,HttpStatus.CREATED);
  }

  @PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
  public ResponseEntity<CartDTO> updateProductInCart(@PathVariable Long cartId,@PathVariable Long productId,
                                                     @PathVariable Integer quantity){
    CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId,productId,quantity);
    return new ResponseEntity<>(cartDTO,HttpStatus.OK);
  }

  @DeleteMapping("/public/carts/{cartId}/products/{productId}")
  public ResponseEntity<String> deleteCartItem(@PathVariable Long cartId,@PathVariable Long productId){
    String message = cartService.deleteProductFromCart(productId,cartId);
    return new ResponseEntity<>(message,HttpStatus.OK);
  }
}
