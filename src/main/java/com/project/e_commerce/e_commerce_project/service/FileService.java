package com.project.e_commerce.e_commerce_project.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {
  String uploadImage(String path, MultipartFile image);
}
