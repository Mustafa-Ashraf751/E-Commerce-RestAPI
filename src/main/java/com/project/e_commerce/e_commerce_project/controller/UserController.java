package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.Dto.UserDTO;
import com.project.e_commerce.e_commerce_project.Dto.UserResponse;
import com.project.e_commerce.e_commerce_project.config.AppConstants;
import com.project.e_commerce.e_commerce_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/admin/add")
  public ResponseEntity<UserDTO> createNewUser(@RequestBody UserDTO userDTO){
    UserDTO user = userService.addNewUser(userDTO);
    return new ResponseEntity<>(user,HttpStatus.CREATED);
  }

  @GetMapping("/admin/users")
  public ResponseEntity<UserResponse> getAllUsers(
          @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
          @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
          @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
          @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder
  ){
    UserResponse userResponse = userService.getUsers(pageNumber,pageSize,sortBy,sortOrder);

    return new ResponseEntity<>(userResponse, HttpStatus.FOUND);
  }

  @GetMapping("/public/users/{id}")
  public ResponseEntity<UserDTO> findUserById(@PathVariable Long id){
    UserDTO user = userService.findUserById(id);
    return new ResponseEntity<>(user,HttpStatus.FOUND);
  }


  @PutMapping("/admin/users/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userId,@RequestBody UserDTO userDTO){
    UserDTO user = userService.updateUser(userId,userDTO);
    return new ResponseEntity<>(user,HttpStatus.OK);
  }

  @DeleteMapping("/admin/users/{id}")
  public ResponseEntity<String> deleteUserById(@PathVariable Long id){
    String status = userService.deleteUser(id);
    return new ResponseEntity<>(status,HttpStatus.OK);
  }



}
