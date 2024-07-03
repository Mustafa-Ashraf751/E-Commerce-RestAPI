package com.project.e_commerce.e_commerce_project.controller;

import com.project.e_commerce.e_commerce_project.Dto.AddressDTO;
import com.project.e_commerce.e_commerce_project.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AddressController {

  private AddressService addressService;

  @Autowired
  public AddressController(AddressService addressService) {
    this.addressService = addressService;
  }

  @PostMapping("/address")
  public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
    AddressDTO address = addressService.createAddress(addressDTO);
    return new ResponseEntity<>(address, HttpStatus.CREATED);
  }

  @GetMapping("/addresses")
  public ResponseEntity<List<AddressDTO>> getAllAddresses(){
    List<AddressDTO> addressDTOS = addressService.getAddresses();
    return new ResponseEntity<>(addressDTOS,HttpStatus.FOUND);
  }

  @GetMapping("/addresses/{id}")
  public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id){
    AddressDTO address = addressService.getAddressById(id);
    return new ResponseEntity<>(address,HttpStatus.FOUND);
  }

  @PutMapping("/addresses/{id}")
  public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id,@RequestBody AddressDTO addressDTO){
      AddressDTO address = addressService.updateAddress(id,addressDTO);
      return new ResponseEntity<>(address,HttpStatus.OK);
  }

  @DeleteMapping("/addresses/{id}")
  public ResponseEntity<String> deleteAddress(@PathVariable Long id){
    String message = addressService.deleteAddressById(id);
    return new ResponseEntity<>(message,HttpStatus.OK);
  }

}
