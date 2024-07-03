package com.project.e_commerce.e_commerce_project.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
  private Long addressId;
  private String buildingName;
  private String city;
  private String state;
  private String country;
  private String pincode;
  private String street;

}
