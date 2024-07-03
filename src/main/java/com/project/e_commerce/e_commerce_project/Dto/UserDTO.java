package com.project.e_commerce.e_commerce_project.Dto;

import com.project.e_commerce.e_commerce_project.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private Long userId;
  private String firstName;
  private String lastName;
  private String password;
  private String email;
  private String mobileNumber;
  private Set<Role> roles = new HashSet<>();
  private AddressDTO address;
  private CartDTO cart;

}
