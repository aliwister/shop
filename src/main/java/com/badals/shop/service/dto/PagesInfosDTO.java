package com.badals.shop.service.dto;

import com.badals.shop.domain.tenant.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PagesInfosDTO implements Serializable {
    String slug;
    List<PageInfo> pageInfos = new ArrayList<>();
}
