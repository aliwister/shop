package com.badals.shop.service;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.SignUpQuestion;
import com.badals.shop.domain.SignUpResponse;
import com.badals.shop.domain.pojo.SignUpQuestionDetails;
import com.badals.shop.repository.SignUpQuestionRepository;
import com.badals.shop.repository.SignUpResponseRepository;
import com.badals.shop.service.dto.AddSignUpResponseInput;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PreOnBoardingService {

    private final Logger log = LoggerFactory.getLogger(PreOnBoardingService.class);

    private final SignUpQuestionRepository signUpQuestionRepository;
    private final SignUpResponseRepository signUpResponseRepository;
    private final CustomerService customerService;

    public void saveSignUpResponse(AddSignUpResponseInput addSignUpResponseInput) {
        SignUpQuestion signUpQuestion = signUpQuestionRepository.findByQuestionCode(addSignUpResponseInput.getQuestionCode()).orElse(null);
        if (signUpQuestion == null) {
            signUpQuestion = SignUpQuestion.builder()
                .questionCode(addSignUpResponseInput.getQuestionCode())
                .question(SignUpQuestionDetails.builder()
                    .question(addSignUpResponseInput.getQuestion())
                    .choices(addSignUpResponseInput.getChoices())
                    .build())
                .build();
            signUpQuestion = signUpQuestionRepository.save(signUpQuestion);
        }
        Customer customer = customerService.getUserWithAuthorities().orElse(null);
        if (customer == null) {
            log.debug("Customer not logged in");
            return;
        }
        for (String responseCode: addSignUpResponseInput.getResponseCodes()) {
            SignUpResponse signUpResponse = signUpResponseRepository.findByCustomerAndQuestionAndResponseCode(customer,signUpQuestion, responseCode);
            if (signUpResponse == null) {
                signUpResponse = SignUpResponse.builder()
                    .customer(customer)
                    .question(signUpQuestion)
                    .responseCode(responseCode)
                    .build();
                signUpResponseRepository.save(signUpResponse);
            }
        }

    }

}
