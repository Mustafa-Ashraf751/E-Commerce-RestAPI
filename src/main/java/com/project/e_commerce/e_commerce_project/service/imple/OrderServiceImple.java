package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.Dto.OrderDTO;
import com.project.e_commerce.e_commerce_project.Dto.OrderResponse;
import com.project.e_commerce.e_commerce_project.entity.*;
import com.project.e_commerce.e_commerce_project.exception.APIException;
import com.project.e_commerce.e_commerce_project.exception.ResourceNotFoundException;
import com.project.e_commerce.e_commerce_project.repository.*;
import com.project.e_commerce.e_commerce_project.service.CartService;
import com.project.e_commerce.e_commerce_project.service.OrderService;
import com.project.e_commerce.e_commerce_project.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImple implements OrderService {

  private final UserRepo userRepo;
  private final CartRepo cartRepo;
  private final OrderRepo orderRepo;
  private final ModelMapper modelMapper;
  private final ProductRepo productRepo;
  private final PaymentRepo paymentRepo;
  private final CartService cartService;

  @Autowired
  public OrderServiceImple(UserRepo userRepo, CartRepo cartRepo,
                           OrderRepo orderRepo, ModelMapper modelMapper,
                           ProductRepo productRepo, PaymentRepo paymentRepo,
                           OrderItemRepo orderItemRepo,CartService cartService) {
    this.userRepo = userRepo;
    this.cartRepo = cartRepo;
    this.orderRepo = orderRepo;
    this.modelMapper = modelMapper;
    this.productRepo = productRepo;
    this.paymentRepo = paymentRepo;
    this.cartService = cartService;
  }


  @Override
  @Transactional
  public OrderDTO createOrder(String userEmail, Long cartId, String paymentMethod) {
    // fetchUserByEmail
    User user = userRepo.findUserByEmail(userEmail).
            orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", userEmail));

    //Fetching cart by cartId
    Cart cart = cartRepo.findById(cartId).
            orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

    //Check for quantity
    cart.getCartItems().forEach(cartItem -> {
      Product product = cartItem.getProduct();
      if (product.getQuantity() < cartItem.getQuantity()) {
        throw new APIException("Sorry product is not valid now");
      }
    });

    //Setting the order from the cart
    Order order = new Order();
    order.setEmail(userEmail);
    order.setTotalAmount(cart.getTotalPrice());
    LocalDate date = LocalDate.now();
    order.setLocalDate(date);

//    //Save the order initially to generate its id
//    Order savedOrder = orderRepo.save(order);
//
//    //Update payment information
//    Payment payment = new Payment();
//    payment.setPaymentMethod(paymentMethod);
//    payment.setOrder(savedOrder);
//    payment = paymentRepo.save(payment);
//    //Update order with payment information
//    savedOrder.setPayment(payment);
//
//    List<OrderItem> orderItems = new ArrayList<>();
//    cart.getCartItems().forEach(cartItem -> {
//              OrderItem orderItem = new OrderItem();
//              orderItem.setOrder(savedOrder);
//              orderItem.setOrderProductPrice(cartItem.getProductPrice());
//              orderItem.setProduct(cartItem.getProduct());
//              orderItem.setQuantity(cartItem.getQuantity());
//              orderItem.setDiscount(cartItem.getDiscount());
//              orderItems.add(orderItem);
//            }
//    );
//
//    //saving the order
//    order.setOrderItems(orderItems);
//    Order finalSave = orderRepo.save(savedOrder);
// Create the payment
    Payment payment = new Payment();
    payment.setPaymentMethod(paymentMethod);
    payment.setOrder(order);
    order.setPayment(payment);

    // Create order items
    List<OrderItem> orderItems = new ArrayList<>();
    for (CartItem cartItem : cart.getCartItems()) {
      OrderItem orderItem = new OrderItem();
      orderItem.setOrder(order);
      orderItem.setOrderProductPrice(cartItem.getProductPrice());
      orderItem.setProduct(cartItem.getProduct());
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setDiscount(cartItem.getDiscount());
      orderItems.add(orderItem);
    }

    // Set order items
    order.setOrderItems(orderItems);

    // Save the order (this should cascade to payment and order items)
    Order savedOrder = orderRepo.save(order);

    //update products quantity
    cart.getCartItems().forEach(cartItem -> {
      Product product = cartItem.getProduct();
      //Remove the ordered item from user cart
      cartService.deleteProductFromCart(cartItem.getProduct().getProductId(),cartId);
      product.setQuantity(product.getQuantity() - cartItem.getQuantity());
      productRepo.save(product);
    });

    //clear the cart after ordering process
    cart.getCartItems().clear();
    cartRepo.save(cart);

    return modelMapper.map(savedOrder, OrderDTO.class);
  }

  @Override
  public OrderResponse getOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder = sortBy.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
            Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Order> orderPage = orderRepo.findAll(pageDetails);
    List<Order> orders = orderPage.getContent();
    List<OrderDTO> orderDTOS = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class)).toList();
    OrderResponse orderResponse = new OrderResponse();
    orderResponse.setContent(orderDTOS);
    orderResponse.setPageNumber(orderPage.getNumber());
    orderResponse.setPageSize(orderPage.getSize());
    orderResponse.setTotalPages(orderPage.getTotalPages());
    orderResponse.setTotalElements((int) orderPage.getTotalElements());
    orderResponse.setLastPage(orderPage.isLast());
    return orderResponse;
  }

  @Override
  public List<OrderDTO> getOrderByEmailId(String email) {
    User user = userRepo.findUserByEmail(email).
            orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", email));

    List<Order> orders = orderRepo.findAllByEmailAddress(email);

    if (orders.isEmpty()) {
      throw new APIException("The user don't place any orders yet");
    }

    return orders.stream().map(order -> modelMapper.map(order, OrderDTO.class)).toList();
  }

  @Override
  public OrderDTO getOrderByEmailIdAndOrderId(String email, Long orderId) {
    User user = userRepo.findUserByEmail(email).
            orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", email));
    Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);
    return modelMapper.map(order, OrderDTO.class);
  }

  @Override
  @Transactional
  public OrderDTO updateOrderStatus(String email, Long orderId, String status) {
    User user = userRepo.findUserByEmail(email).
            orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", email));
    Order order = orderRepo.findById(orderId).
            orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));
    order.setOrderStatus(OrderStatus.valueOf(status));
    Order savedOrder = orderRepo.save(order);
    return modelMapper.map(savedOrder, OrderDTO.class);
  }


}
