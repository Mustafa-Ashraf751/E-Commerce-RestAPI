package com.project.e_commerce.e_commerce_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Size(min = 5, max = 30, message = "First Name must be between 5 and 30 characters long")
  @Pattern(regexp = "^[a-zA-Z]{5,30}$", message = "First name must not contain numbers or any special characters")
  private String firstName;

  @Size(min = 5, max = 30, message = "First Name must be between 5 and 30 characters long")
  @Pattern(regexp = "^[a-zA-Z]{5,30}$", message = "First name must not contain numbers or any special characters")
  private String lastName;

  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @Size(min = 10, max = 10, message = "Mobile number must be exactly 10 digits long")
  @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be exactly 10 digits long")
  private String mobileNumber;

  private String password;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "user_address"
          , joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "address_id")
  )
  private List<Address> addresses = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  @JoinTable(name = "user_role"
          , joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
  private Cart cart;


}
