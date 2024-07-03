package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.Dto.CategoryDTO;
import com.project.e_commerce.e_commerce_project.Dto.CategoryResponse;
import com.project.e_commerce.e_commerce_project.entity.Category;
import com.project.e_commerce.e_commerce_project.entity.Product;
import com.project.e_commerce.e_commerce_project.exception.APIException;
import com.project.e_commerce.e_commerce_project.exception.ResourceNotFoundException;
import com.project.e_commerce.e_commerce_project.repository.CategoryRepo;
import com.project.e_commerce.e_commerce_project.service.CategoryService;
import com.project.e_commerce.e_commerce_project.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImple implements CategoryService {

  private final CategoryRepo categoryRepo;
  private final ModelMapper modelMapper;
  private final ProductService productService;

  @Autowired
  public CategoryServiceImple(CategoryRepo categoryRepo,
                              ModelMapper modelMapper,ProductService productService) {
    this.categoryRepo = categoryRepo;
    this.modelMapper = modelMapper;
    this.productService = productService;
  }

  @Override
  public CategoryDTO createCategory(Category category) {
    Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());
    if(savedCategory != null){
      throw new APIException("Category with name "+category.getCategoryName()+" already exists!");
    }
    savedCategory = new Category();
    savedCategory.setCategoryName(category.getCategoryName());
    savedCategory = categoryRepo.save(savedCategory);

    return modelMapper.map(savedCategory,CategoryDTO.class);
  }

  @Override
  public CategoryDTO updateCategory(Category category, Long categoryId) {
    Category category1 = categoryRepo.findById(categoryId).
            orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

    category1.setCategoryName(category.getCategoryName());
   Category updatedCategory = categoryRepo.save(category1);

    return modelMapper.map(updatedCategory,CategoryDTO.class);
  }

  @Override
  public CategoryResponse getCategories(Integer pageNumber , Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
    Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
    Page<Category> page = categoryRepo.findAll(pageDetails);

    List<Category> categories = page.getContent();
    if(categories == null){
      throw new APIException("No categories created until now");
    }
    List<CategoryDTO> categoryDTOS = categories.stream().
            map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
    CategoryResponse categoryResponse = new CategoryResponse();
    categoryResponse.setContent(categoryDTOS);
    categoryResponse.setPageNumber(page.getNumber());
    categoryResponse.setPageSize(page.getSize());
    categoryResponse.setTotalPages(page.getTotalPages());
    categoryResponse.setTotalElement(page.getNumberOfElements());
    categoryResponse.setLastPage(page.isLast());

    return categoryResponse;
  }

  @Override
  public String deleteCategory(Long categoryId) {
    Category category = categoryRepo.findById(categoryId).
            orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
    List<Product> products = category.getProducts();

    products.forEach(product-> productService.deleteProduct(product.getProductId()));

    categoryRepo.delete(category);

    return "Category with id:+"+ categoryId +" deleted successfully!";
  }


}
