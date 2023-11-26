package com.badals.shop.domain.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RewardRules implements Serializable {
    private Integer minCartAmount;
}
