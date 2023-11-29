package com.badals.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "signup_response", catalog = "profileshop")
public class SignUpResponse implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "response_code")
        private String responseCode;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "tutorial_id", nullable = false)
        @JsonIgnore
        private SignUpQuestion question;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "customer_id", nullable = false)
        private Customer customer;

}
