package com.badals.shop.graph;

import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.dto.PaymentDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HashtagResponse extends MutationResponse implements Serializable {
    List<HashtagDTO> items;
    int total;
    boolean hasMore;
}
