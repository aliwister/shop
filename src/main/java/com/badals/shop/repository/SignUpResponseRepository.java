package com.badals.shop.repository;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.SignUpQuestion;
import com.badals.shop.domain.SignUpResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpResponseRepository extends JpaRepository<SignUpResponse, Long> {
    SignUpResponse findByCustomerAndQuestionAndResponseCode(Customer customer, SignUpQuestion question, String responseCode);
}
