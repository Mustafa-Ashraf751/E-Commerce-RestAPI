package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Long> {
  Address findByCityAndCountryAndStateAndBuildingNameAndPincodeAndStreet(String street,String buildingName,String state,
                                                               String pincode,String country,String city);
}
