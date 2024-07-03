package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.Dto.UserDTO;
import com.project.e_commerce.e_commerce_project.Dto.UserResponse;

public interface UserService {

  UserDTO addNewUser(UserDTO userDTO);
  UserDTO findUserById(Long id);
  UserResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy,String sortOrder);
  UserDTO updateUser(Long id , UserDTO userDTO);
  String deleteUser(Long id);
}
