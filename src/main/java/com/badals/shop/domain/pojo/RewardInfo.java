package com.badals.shop.domain.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RewardInfo implements Serializable {
    private String language;
    private String name;
    private String description;
}

