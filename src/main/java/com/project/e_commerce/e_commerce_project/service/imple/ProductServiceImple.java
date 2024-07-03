package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.Dto.CartDTO;
import com.project.e_commerce.e_commerce_project.Dto.ProductDTO;
import com.project.e_commerce.e_commerce_project.Dto.ProductResponse;
import com.project.e_commerce.e_commerce_project.entity.Cart;
import com.project.e_commerce.e_commerce_project.entity.Category;
import com.project.e_commerce.e_commerce_project.entity.Product;
import com.project.e_commerce.e_commerce_project.exception.APIException;
import com.project.e_commerce.e_commerce_project.exception.ResourceNotFoundException;
import com.project.e_commerce.e_commerce_project.repository.CartRepo;
import com.project.e_commerce.e_commerce_project.repository.CategoryRepo;
import com.project.e_commerce.e_commerce_project.repository.ProductRepo;
import com.project.e_commerce.e_commerce_project.service.CartService;
import com.project.e_commerce.e_commerce_project.service.FileService;
import com.project.e_commerce.e_commerce_project.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImple implements ProductService {

  private ProductRepo productRepo;
  private CartRepo cartRepo;
  private CategoryRepo categoryRepo;
  private CartService cartService;
  private ModelMapper modelMapper;
  @Value("${project.image}")
  private String path;
  private FileService fileService;

  @Autowired
  public ProductServiceImple(ProductRepo productRepo, CartRepo cartRepo,
                             CategoryRepo categoryRepo, CartService cartService,
                             ModelMapper modelMapper, @Value("${project.image}") String path,
                             FileService fileService) {
    this.productRepo = productRepo;
    this.cartRepo = cartRepo;
    this.categoryRepo = categoryRepo;
    this.cartService = cartService;
    this.modelMapper = modelMapper;
    this.path = path;
    this.fileService = fileService;
  }

  @Override
  public ProductDTO createProduct(Long categoryId, Product product) {
    Category category = categoryRepo.findById(categoryId).
            orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));
    List<Product> products = category.getProducts();
     boolean productExist = products.stream().anyMatch(product1 ->
            product1.getProductName().equals(product.getProductName()) &&
            product1.getDescription().equals(product.getDescription())
    );
     if (productExist){
       throw new APIException("The product is already exist");
     }
     product.setImage("default.png");
     product.setCategory(category);
     double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01 ) * product.getPrice());
     product.setSpecialPrice(specialPrice);
     Product savedProduct = productRepo.save(product);
     return modelMapper.map(savedProduct, ProductDTO.class);

  }

  @Override
  public ProductResponse getAllProducts(Integer pageNumber,
                                        Integer pageSize, String sortBy,
                                        String sortOrder) {
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
            Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
    Page<Product> productPage = productRepo.findAll(pageDetails);
    if (productPage.isEmpty()){
      throw new APIException("No products added yet!");
    }
    List<ProductDTO> productDTOS = productPage.getContent().stream().
            map(product -> modelMapper.map(product,ProductDTO.class)).
            toList();

    return createProductResponse(productDTOS,productPage);
  }

  @Override
  public ProductResponse getProductsByCategoryId(Long categoryId,
                                                  Integer pageNumber,Integer pageSize,
                                                  String sortBy,String sortOrder) {
    Category category = categoryRepo.findById(categoryId).
            orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",categoryId));
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
            Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
    Page<Product> productPage = productRepo.findByCategoryId(categoryId,pageDetails);
    List<Product> products = productPage.getContent();

    List<ProductDTO> productDTOS = products.stream().
            map(product -> modelMapper.map(product,ProductDTO.class)).
            toList();

    return createProductResponse(productDTOS,productPage);
  }

  @Override
  public ProductResponse getProductByKeyWord(String keyWord, Integer pageNumber,
                                             Integer pageSize, String sortBy,
                                             String sortOrder) {
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
            Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
    Page<Product> productPage = productRepo.findByKeyWord(keyWord,pageDetails);
    List<Product> products = productPage.getContent();
    if(products.isEmpty()){
      throw new APIException("The product you want not found");
    }
    List<ProductDTO> productDTOS = products.stream().
            map(product -> modelMapper.map(product,ProductDTO.class)).
            toList();
    return createProductResponse(productDTOS,productPage);
  }

  @Override
  public ProductDTO updateProductById(Long productId,Product product) {
    Product productDB = productRepo.findById(productId).orElseThrow(()->
            new ResourceNotFoundException("Product", "productId",productId));
    product.setImage(productDB.getImage());
    product.setCategory(productDB.getCategory());
    double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
    product.setSpecialPrice(specialPrice);
    Product savedProduct = productRepo.save(product);
    List<Cart> carts = cartRepo.findCartsByProductId(productId);
    List<CartDTO> cartDTOS = carts.stream().
            map(cart -> {
             CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
             List<ProductDTO> productDTOS = cart.getCartItems().stream().
                     map(item -> modelMapper.map(item.getProduct(),ProductDTO.class)).toList();
             cartDTO.setProducts(productDTOS);
             return cartDTO;
            }).toList();

    cartDTOS.forEach(cart -> cartService.updateProductInCart(cart.getCartId(),productId));

    return modelMapper.map(savedProduct,ProductDTO.class);
  }

  @Override
  public ProductDTO updateProductImage(MultipartFile image, Long productId) {
    Product productDB = productRepo.findById(productId).orElseThrow(()->
            new ResourceNotFoundException("Product", "productId",productId));
    String fileName = fileService.uploadImage(path,image);
    productDB.setImage(fileName);
    Product savedProduct = productRepo.save(productDB);
    return modelMapper.map(savedProduct,ProductDTO.class);
  }

  @Override
  public String deleteProduct(Long productId) {
    Product productDB = productRepo.findById(productId).orElseThrow(()->
            new ResourceNotFoundException("Product", "productId",productId));
    List<Cart> cartDB = cartRepo.findCartsByProductId(productId);
    cartDB.forEach(cart -> cartService.deleteProductFromCart(productId,cart.getCartId()));
    productRepo.delete(productDB);
    return "The product deleted successfully!";
  }

  public ProductResponse createProductResponse(List<ProductDTO> productDTOS,Page<Product> productPage){
    ProductResponse productResponse = new ProductResponse();
    productResponse.setContent(productDTOS);
    productResponse.setPageNumber(productPage.getNumber());
    productResponse.setPageSize(productPage.getSize());
    productResponse.setTotalPages(productPage.getTotalPages());
    productResponse.setTotalElements((int)productPage.getTotalElements());
    productResponse.setLastPage(productPage.isLast());
    return productResponse;
  }
}
