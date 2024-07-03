package com.project.e_commerce.e_commerce_project.service;

import com.project.e_commerce.e_commerce_project.Dto.AddressDTO;

import java.util.List;

public interface AddressService {

  AddressDTO createAddress(AddressDTO addressDTO);
  List<AddressDTO> getAddresses();
  AddressDTO getAddressById(Long id);
  AddressDTO updateAddress(Long id,AddressDTO addressDTO);
  String deleteAddressById(Long id);

}
