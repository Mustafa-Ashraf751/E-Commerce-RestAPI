package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.Dto.CartDTO;
import com.project.e_commerce.e_commerce_project.Dto.ProductDTO;
import com.project.e_commerce.e_commerce_project.entity.Cart;
import com.project.e_commerce.e_commerce_project.entity.CartItem;
import com.project.e_commerce.e_commerce_project.entity.Product;
import com.project.e_commerce.e_commerce_project.exception.APIException;
import com.project.e_commerce.e_commerce_project.exception.ResourceNotFoundException;
import com.project.e_commerce.e_commerce_project.repository.CartItemRepo;
import com.project.e_commerce.e_commerce_project.repository.CartRepo;
import com.project.e_commerce.e_commerce_project.repository.ProductRepo;
import com.project.e_commerce.e_commerce_project.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImple implements CartService {

  private CartRepo cartRepo;
  private ProductRepo productRepo;
  private CartItemRepo cartItemRepo;
  private ModelMapper modelMapper;

  @Autowired
  public CartServiceImple(CartRepo cartRepo, ProductRepo productRepo,
                          CartItemRepo cartItemRepo, ModelMapper modelMapper) {
    this.cartRepo = cartRepo;
    this.productRepo = productRepo;
    this.cartItemRepo = cartItemRepo;
    this.modelMapper = modelMapper;
  }

  @Override
  @Transactional
  public CartDTO addProductsToCarts(Long cartId, Long productId, Integer quantity) {
    Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
    Product product = productRepo.findById(productId).
            orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
    CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

    if (product.getQuantity() == 0) {
      throw new APIException("Product " + product.getProductName() + " is not valid now");
    }
    if (product.getQuantity() < quantity) {
      throw new APIException("Please make an order of " + product.getProductName() +
              " equal than or less " + product.getQuantity());
    }
    if (cartItem == null) {
      CartItem newCartItem = new CartItem();
      newCartItem.setCart(cart);
      newCartItem.setProduct(product);
      newCartItem.setQuantity(quantity);
      newCartItem.setProductPrice(product.getSpecialPrice());
      newCartItem.setDiscount(product.getDiscount());
      cartItemRepo.save(newCartItem);
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
      cartItemRepo.save(cartItem);

    }

    product.setQuantity(product.getQuantity() - quantity);
    cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
    List<ProductDTO> productDTOS = cart.getCartItems().stream().
            map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
    cartDTO.setProducts(productDTOS);
    return cartDTO;
  }

  @Override
  public CartDTO getCart(String emailId, Long cartId) {
    Cart cart = cartRepo.findCartByIdAndEmailId(cartId, emailId);
    if (cart == null) {
      throw new ResourceNotFoundException("Cart", "cartId", cartId);
    }
    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
    List<ProductDTO> productDTOS = cart.getCartItems().stream()
            .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
    cartDTO.setProducts(productDTOS);
    return cartDTO;
  }

  @Override
  @Transactional
  public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
    Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
    Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product",
            "productId", productId));
    CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
    if (cartItem == null) {
      throw new APIException("Product " + product.getProductName() + " is not available in cart");
    }
    if (product.getQuantity() == 0) {
      throw new APIException("Product " + product.getProductName() + " not available now");
    }
    if (product.getQuantity() < quantity) {
      throw new APIException("Please make an order of " + product.getProductName() +
              " equal than or less " + product.getQuantity());
    }

    // Calculate the current total price without the cart item
    double oldItemTotalPrice = cartItem.getProductPrice() * cartItem.getQuantity();
    double newItemTotalPrice = product.getSpecialPrice() * quantity;


    // Update the product's quantity in the inventory
    int quantityDifference = cartItem.getQuantity() - quantity;
    product.setQuantity(product.getQuantity() + quantityDifference);
    productRepo.save(product);

    // Update the cart item's quantity and price
    cartItem.setQuantity(quantity);
    cartItem.setProductPrice(product.getSpecialPrice());
    cartItem.setDiscount(product.getDiscount());
    cartItemRepo.save(cartItem);

    // Update the cart's total price
    double updatedTotalPrice = cart.getTotalPrice() - oldItemTotalPrice + newItemTotalPrice;


    cart.setTotalPrice(updatedTotalPrice);
    cartRepo.save(cart);


    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
    List<ProductDTO> productDTOS = cart.getCartItems().stream().
            map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
    cartDTO.setProducts(productDTOS);
    return cartDTO;
  }

  @Override
  public void updateProductInCart(Long cartId, Long productId) {
    Cart cart = cartRepo.findById(cartId).
            orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
    Product product = productRepo.findById(productId).
            orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
    CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
    if (cartItem == null) {
      throw new APIException("Product " + product.getProductName() + " is not available in cart");
    }
    double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
    cartItem.setProductPrice(product.getSpecialPrice());
    cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
    cartItem = cartItemRepo.save(cartItem);
  }

  @Override
  @Transactional
  public String deleteProductFromCart(Long productId, Long cartId) {
    Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart", "cartId", cartId));
    CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
    if (cartItem == null) {
      throw new ResourceNotFoundException("product", "productId", productId);
    }
    // Removing the price of product from the user cart
    cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
    // change the quantity in inventory after user delete the product
    Product product = cartItem.getProduct();
    product.setQuantity(product.getQuantity() + cartItem.getQuantity());

    // deleting the cartItem that include the product
    cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

    return "The product " + cartItem.getProduct().getProductName() + "is removed successfully!";
  }

  @Override
  public List<CartDTO> getAllCarts() {
    List<Cart> carts = cartRepo.findAll();
    if (carts.isEmpty()) {
      throw new ResourceNotFoundException("No carts found");
    }
    List<CartDTO> cartDTOS = carts.stream().map(cart -> {
      CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
      List<ProductDTO> productDTOs = cart.getCartItems().stream().
              map(cartItem -> modelMapper.map(cartItem.getProduct(), ProductDTO.class)).toList();
      cartDTO.setProducts(productDTOs);
      return cartDTO;
    }).toList();

    return cartDTOS;
  }


}
