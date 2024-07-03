package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.Dto.*;
import com.project.e_commerce.e_commerce_project.entity.*;
import com.project.e_commerce.e_commerce_project.exception.APIException;
import com.project.e_commerce.e_commerce_project.exception.ResourceNotFoundException;
import com.project.e_commerce.e_commerce_project.repository.AddressRepo;
import com.project.e_commerce.e_commerce_project.repository.CartRepo;
import com.project.e_commerce.e_commerce_project.repository.RoleRepo;
import com.project.e_commerce.e_commerce_project.repository.UserRepo;
import com.project.e_commerce.e_commerce_project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImple implements UserService {

  private UserRepo userRepo;

  private ModelMapper modelMapper;

  private AddressRepo addressRepo;

  private RoleRepo roleRepo;

  private PasswordEncoder passwordEncoder;

  private CartRepo cartRepo;

  @Autowired
  public UserServiceImple(UserRepo userRepo, ModelMapper modelMapper,
                          AddressRepo addressRepo, RoleRepo roleRepo,
                          PasswordEncoder passwordEncoder,CartRepo cartRepo) {
    this.userRepo = userRepo;
    this.modelMapper = modelMapper;
    this.addressRepo = addressRepo;
    this.roleRepo = roleRepo;
    this.passwordEncoder = passwordEncoder;
    this.cartRepo = cartRepo;
  }

  @Override
  public UserDTO addNewUser(UserDTO userDTO) {
    try {
      User user = modelMapper.map(userDTO,User.class);
      //Setting empty cart to new user
      Cart cart = new Cart();
      user.setCart(cart);
      // Setting the user role to new user
//      Role role = roleRepo.findById(AppConstants.USER_ID).get();
      Role role = new Role();
      role.setRoleName("ADMIN");
      user.getRoles().add(role);
      //Saving the user address to database
      String country = userDTO.getAddress().getCountry();
      String street = userDTO.getAddress().getStreet();
      String city = userDTO.getAddress().getCity();
      String pincode = userDTO.getAddress().getPincode();
      String buildingName = userDTO.getAddress().getBuildingName();
      String state = userDTO.getAddress().getState();

      Address address = addressRepo.findByCityAndCountryAndStateAndBuildingNameAndPincodeAndStreet
              (city,country,street,pincode,buildingName,state);

      if (address == null){
        address = new Address(city,country,street,pincode,buildingName,state);
        addressRepo.save(address);
      }

      user.setAddresses(List.of(address));
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      // Saving the user to database
      User newUser = userRepo.save(user);
      // Adding the user to cart
      cart.setUser(newUser);
      cartRepo.save(cart);
      //Covert the newUser to dto
      userDTO = modelMapper.map(newUser,UserDTO.class);
      userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
      return userDTO;
    } catch (DataIntegrityViolationException e) {
      throw new APIException("The user already exist with emailId: "+userDTO.getEmail());
    }
  }

  @Override
  public UserResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
    Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
    Page<User> pageUsers = userRepo.findAll(pageDetails);
    List<User> users = pageUsers.getContent();
   if(users.size() == 0){
     throw new APIException("No users exist");
   }
   List<UserDTO> userDTOS = users.stream().map(user -> {
     UserDTO dto = modelMapper.map(user, UserDTO.class);
     if(user.getAddresses().size() != 0 ){
        dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(),AddressDTO.class));
     }
     CartDTO cart = modelMapper.map(user.getCart(),CartDTO.class);
     List<ProductDTO> products = user.getCart().getCartItems().stream().map(item -> modelMapper.map(item.getProduct()
             ,ProductDTO.class)).toList();

     dto.setCart(cart);
     dto.getCart().setProducts(products);

     return dto;

   }).toList();
   UserResponse userResponse = new UserResponse();
   userResponse.setContent(userDTOS);
   userResponse.setPageNumber(pageUsers.getNumber());
   userResponse.setPageSize(pageUsers.getSize());
   userResponse.setTotalElement(pageUsers.getNumberOfElements());
   userResponse.setLastPage(pageUsers.isLast());

    return userResponse;
  }


  @Override
  public UserDTO findUserById(Long id) {
    User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","userId",id));
    UserDTO userDTO = modelMapper.map(user,UserDTO.class);
    userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(),AddressDTO.class));
    CartDTO cart = modelMapper.map(user.getCart(),CartDTO.class);
    List<ProductDTO> products = user.getCart().getCartItems().stream().map(item ->
            modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
    userDTO.setCart(cart);
    userDTO.getCart().setProducts(products);
    return userDTO;
  }

  @Override
  public UserDTO updateUser(Long id, UserDTO userDTO) {
    User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","userId",id));
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    //Updating the user fields except the adress
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(encodedPassword);
    user.setMobileNumber(userDTO.getMobileNumber());
    // Updating the address of the user
    if(userDTO.getAddress() != null){
      String buildingName = userDTO.getAddress().getBuildingName();
      String city = userDTO.getAddress().getCity();
      String state = userDTO.getAddress().getState();
      String country = userDTO.getAddress().getCountry();
      String pincode = userDTO.getAddress().getPincode();
      String street = userDTO.getAddress().getStreet();
      Address address = addressRepo.findByCityAndCountryAndStateAndBuildingNameAndPincodeAndStreet(buildingName
              ,city,state,country,pincode,street);
      if(address == null){
        address = new Address(buildingName,city,country,pincode,street,state);
        addressRepo.save(address);
        user.setAddresses(List.of(address));
      }
    }
    //convert the user to userDTO to back it to the user
    userDTO = modelMapper.map(user, UserDTO.class);
    userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(),AddressDTO.class));
    CartDTO cart = modelMapper.map(user.getCart(),CartDTO.class);
    List<ProductDTO> products = user.getCart().getCartItems().stream().map(item ->
            modelMapper.map(item.getProduct(), ProductDTO.class)).toList();
    userDTO.setCart(cart);
    userDTO.getCart().setProducts(products);

    return userDTO;
  }

  @Override
  public String deleteUser(Long id) {
    User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User","userId",id));
    List<CartItem> cartItems = user.getCart().getCartItems();
    Long cartId = user.getCart().getCartId();



    userRepo.delete(user);
    return "User with id: "+ id + "deleted successfully!";
  }


}
