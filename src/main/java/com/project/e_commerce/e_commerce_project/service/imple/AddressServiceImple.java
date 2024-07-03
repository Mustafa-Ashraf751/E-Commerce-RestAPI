package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.Dto.AddressDTO;
import com.project.e_commerce.e_commerce_project.entity.Address;
import com.project.e_commerce.e_commerce_project.entity.User;
import com.project.e_commerce.e_commerce_project.exception.ResourceNotFoundException;
import com.project.e_commerce.e_commerce_project.repository.AddressRepo;
import com.project.e_commerce.e_commerce_project.repository.UserRepo;
import com.project.e_commerce.e_commerce_project.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImple implements AddressService {

  private final AddressRepo addressRepo;
  private final UserRepo userRepo;
  private final ModelMapper modelMapper;

  @Autowired
  public AddressServiceImple(AddressRepo addressRepo, UserRepo userRepo,ModelMapper modelMapper) {
    this.addressRepo = addressRepo;
    this.userRepo = userRepo;
    this.modelMapper = modelMapper;
  }

  @Override
  public AddressDTO createAddress(AddressDTO addressDTO) {
    // Getting the data from addressDto
    String buildingName = addressDTO.getBuildingName();
    String country = addressDTO.getCountry();
    String city = addressDTO.getCity();
    String state = addressDTO.getState();
    String pincode = addressDTO.getPincode();
    String street = addressDTO.getStreet();

    Address address = addressRepo.findByCityAndCountryAndStateAndBuildingNameAndPincodeAndStreet(street,
            buildingName,state,pincode,country,city);
    if(address == null){
      address = new Address(buildingName,city,country,pincode,street,state);
    }

    Address savedAddress = addressRepo.save(address);

    return modelMapper.map(savedAddress,AddressDTO.class) ;
  }

  @Override
  public List<AddressDTO> getAddresses() {
    List<Address> addresses = null;
    try {
      addresses = addressRepo.findAll();
    } catch (ResourceNotFoundException e) {
      throw new ResourceNotFoundException("No addresses found");
    }
    List<AddressDTO> addressDTOS = addresses.stream().
            map(address -> modelMapper.map(address,AddressDTO.class)).toList();
    return addressDTOS;
  }

  @Override
  public AddressDTO getAddressById(Long id) {
    Address address = addressRepo.findById(id).orElseThrow(()->
            new ResourceNotFoundException("Address", "addressId", id ));
    AddressDTO addressDTO = modelMapper.map(address,AddressDTO.class);
    return addressDTO;
  }

  @Override
  public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
    Address address = addressRepo.findByCityAndCountryAndStateAndBuildingNameAndPincodeAndStreet(
      addressDTO.getStreet(),addressDTO.getBuildingName(),addressDTO.getState(),addressDTO.getPincode(),
      addressDTO.getCountry(),addressDTO.getCity()
    );

    if(address == null){
      address = addressRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Address","addressId",id));
      address.setCity(addressDTO.getCity());
      address.setState(addressDTO.getState());
      address.setPincode(addressDTO.getPincode());
      address.setBuildingName(addressDTO.getBuildingName());
      address.setCountry(addressDTO.getCountry());
      address.setStreet(addressDTO.getStreet());
      Address updatedAddress = addressRepo.save(address);
      return modelMapper.map(updatedAddress,AddressDTO.class);
    }else{
      return modelMapper.map(address,AddressDTO.class);
    }
  }

  @Override
  public String deleteAddressById(Long id) {
    Address address = addressRepo.findById(id).orElseThrow(()->
            new ResourceNotFoundException("Address","addressId",id));
    List<User> users = userRepo.findByAddress(id);
    users.forEach(user ->{
            user.getAddresses().remove(address);
            userRepo.save(user);
  } );
    addressRepo.deleteById(id);
    return "The address has deleted Successfully!";
  }


}
