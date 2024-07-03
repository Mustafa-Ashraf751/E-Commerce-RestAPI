package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.Dto.OrderDTO;
import com.project.e_commerce.e_commerce_project.Dto.OrderResponse;
import com.project.e_commerce.e_commerce_project.config.AppConstants;
import com.project.e_commerce.e_commerce_project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
  public ResponseEntity<OrderDTO> createOrder(@PathVariable String emailId,@PathVariable Long cartId,
                                              @PathVariable String paymentMethod){
    OrderDTO orderDTO = orderService.createOrder(emailId,cartId,paymentMethod);

    return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
  }

  @PutMapping("/admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
  public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String emailId ,@PathVariable Long orderId,
                                                    @PathVariable String orderStatus){
    OrderDTO orderDTO = orderService.updateOrderStatus(emailId,orderId,orderStatus);

    return new ResponseEntity<>(orderDTO,HttpStatus.OK);
  }

  @GetMapping("/admin/orders")
  public ResponseEntity<OrderResponse> getAllOrders(
   @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
   @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
   @RequestParam(name = "sortBy",defaultValue = "localDate",required = false) String sortBy,
   @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder){

    OrderResponse orderResponse = orderService.getOrders(pageNumber,pageSize,sortBy,sortOrder);
    return new ResponseEntity<>(orderResponse,HttpStatus.FOUND);

  }

  @GetMapping("/public/users/{emailId}/orders")
  public ResponseEntity<List<OrderDTO>> getOrdersByEmail(@PathVariable String emailId){
    List<OrderDTO> orderDTOS = orderService.getOrderByEmailId(emailId);
    return new ResponseEntity<>(orderDTOS,HttpStatus.FOUND);
  }

  @GetMapping("/public/users/{emailId}/orders/{orderId}")
  public ResponseEntity<OrderDTO> getOrdersByEmailAndOrderId(@PathVariable String emailId,
                                                                   @PathVariable Long orderId){
    OrderDTO orderDTO = orderService.getOrderByEmailIdAndOrderId(emailId,orderId);
    return new ResponseEntity<>(orderDTO,HttpStatus.FOUND);
  }
}
