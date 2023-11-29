package com.badals.shop.repository;

import com.badals.shop.domain.SignUpQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpQuestionRepository extends JpaRepository<SignUpQuestion, Long> {
    Optional<SignUpQuestion> findByQuestionCode(String questionCode);
}
