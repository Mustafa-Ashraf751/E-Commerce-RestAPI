package com.project.e_commerce.e_commerce_project.repository;

import com.project.e_commerce.e_commerce_project.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {



}
