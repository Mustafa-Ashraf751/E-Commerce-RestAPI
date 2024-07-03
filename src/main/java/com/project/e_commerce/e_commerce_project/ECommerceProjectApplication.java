package com.project.e_commerce.e_commerce_project;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ECommerceProjectApplication {

  @Bean
  public ModelMapper ModelMapperConfig(){
    return new ModelMapper();
  }

	public static void main(String[] args) {
		SpringApplication.run(ECommerceProjectApplication.class, args);
	}

}
