package com.badals.shop.domain.pojo;

import com.badals.shop.service.dto.SignupChoiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpQuestionDetails implements Serializable {

    private String question;
    private List<SignupChoiceDTO> choices;

}
