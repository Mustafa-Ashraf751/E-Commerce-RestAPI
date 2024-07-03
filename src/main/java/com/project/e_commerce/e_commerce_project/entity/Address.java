package com.project.e_commerce.e_commerce_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long addressId;

  @NotBlank
  @Size(min = 5, message = "Building name must have at least 5 characters")
  private String buildingName;

  @NotBlank
  @Size(min = 4, message = "City name must have at least 4 characters")
  private String city;

  @NotBlank
  @Size(min = 2, message = "City name must have at least 2 characters")
  private String country;

  @NotBlank
  @Size(min = 6, message = "Pincode name must have at least 6 characters")
  private String pincode;

  @NotBlank
  @Size(min = 2, message = "State name must have at least 2 characters")
  private String state;

  @NotBlank
  @Size(min = 5, message = "Street name must have at least 5 characters")
  private String street;

 @ManyToMany(mappedBy = "addresses")
  private List<User> users = new ArrayList<>();

  public Address(String buildingName, String city, String country, String pincode, String street, String state) {
    this.buildingName = buildingName;
    this.city = city;
    this.country = country;
    this.pincode = pincode;
    this.street = street;
    this.state = state;
  }
}
