package com.project.e_commerce.e_commerce_project.service.imple;

import com.project.e_commerce.e_commerce_project.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImple implements FileService {
  @Override
  public String uploadImage(String path, MultipartFile image) {
    return "";
  }
}
